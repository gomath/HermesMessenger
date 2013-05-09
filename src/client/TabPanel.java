package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TabPanel extends JPanel {
    private Conversation convo;
    private DefaultListModel historyModel;
    private JScrollPane historyScroll;
    private JList history;
    
    public TabPanel(Conversation convo) {
        this.convo = convo;
        
        //CONERSATION PANEL
        makePanel();    
    }
    
    /**
     * Makes a panel for a conversation
     * @param convo, the conversation associated with that tab
     * @return 
     * @return the panel
     */
    private void makePanel() {
        historyModel = new DefaultListModel();
        history = new JList(historyModel);
        history.setLayoutOrientation(JList.VERTICAL);
        history.setFocusable(false);
        history.setCellRenderer(new MessageRenderer());
        historyScroll = new JScrollPane(history);
        
        //MESSAGE
        final JTextField message = new JTextField();
        message.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String msg = message.getText();
                if (!msg.equals("")) {
                    message.setText("");
                    User.addMsgToConvo(convo, msg);
                    showMessage();
                }
            }
        });
        
        //SEND BUTTON
        JButton sendButton = new JButton();
        sendButton.setText("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String msg = message.getText();
                if (!msg.equals("")) {
                    message.setText("");
                    User.addMsgToConvo(convo, msg);
                    showMessage();
                }
            }
        });
        
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(message, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.LINE_END);
        
        //JPanel panel = new JPanel(new BorderLayout());
        setLayout(new BorderLayout());
        add(historyScroll, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.PAGE_END);
        //return panel;
    }
    
    /**
     * returns conversation
     * @return convo the conversation
     */
    public Conversation getConvo() {
        return convo;
    }
    
    /**
     * fills the history with the messages from the conversation
     */
    public void showMessage() {
        Message msg = convo.getMessages().get(convo.getMessages().size()-1);
        historyModel.addElement(msg.getSender().getUsername() + ": " + msg.getText());
        historyScroll.getVerticalScrollBar().setValue(historyScroll.getVerticalScrollBar().getMaximum());
    }
    
    /**
     * fills in all conversation history
     */
    public void fillHistory() {
        for (Message msg : convo.getMessages()) {
            historyModel.addElement(msg.getSender().getUsername() + ": " + msg.getText());
            historyScroll.getVerticalScrollBar().setValue(historyScroll.getVerticalScrollBar().getMaximum());
        }
    }
    
    /**
     * Renders messages with colors of senders
     *
     */
    private class MessageRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {  
            Component c = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );  
            c.setBackground(ConversationView.colorMap.get(convo.getMessages().get(index).getSender().getColor()));
            c.setForeground(Color.black);
            return c;  
        }
    }
}
