package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TabPanel extends JPanel {
    private Conversation convo;
    private final Color color;
    private DefaultListModel historyModel;
    private JScrollPane historyScroll;
    private JList history;
    private final static ConcurrentHashMap<String, Color> colorMap = new ConcurrentHashMap<String, Color>();
    
    public TabPanel(Conversation convo) {
        //Make color map
        colorMap.put("red", Color.red);
        colorMap.put("orange", Color.orange);
        colorMap.put("yellow", Color.yellow);
        colorMap.put("green", Color.green);
        colorMap.put("blue", Color.blue);
        colorMap.put("pink", Color.pink);
        
        this.convo = convo;
        this.color = getColorforConvo();
        
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
                    fillHistory();
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
                    fillHistory();
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
     * Returns the appropriate color for a conversation based on user colors
     * @return the color to use
     */
    private Color getColorforConvo() {
        String firstUN = ConversationView.parseConvoID(convo.getConvoID()).split(" ")[0];
        ConcurrentHashMap<String, UserInfo> users = User.getOnlineUsers();
        return colorMap.get(users.get(firstUN).getColor());
    }
    
    /**
     * returns color for this convo panel
     * @return color
     */
    public Color getColor() {
        return this.color;
        
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
    public void fillHistory() {
        Message msg = convo.getMessages().get(convo.getMessages().size()-1);
        historyModel.addElement(msg.getSender().getUsername() + ": " + msg.getText());
    }
    
}
