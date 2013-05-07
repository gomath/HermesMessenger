package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import exceptions.DuplicateConvoException;

public class ConversationView extends JPanel{
    private static JTabbedPane tabby;
    private static JMenuBar menuBar;
    private final JMenu file;
    private final JMenuItem logout;
    private static DefaultListModel historyModel;
    private static JScrollPane historyScroll;
    private static JList history;
    private static DefaultListModel listModel;
    private static JList list;
    private final static ConcurrentHashMap<String, Color> colorMap = new ConcurrentHashMap<String, Color>();
    private static ConcurrentHashMap<Integer, Conversation> tabMap = new ConcurrentHashMap<Integer, Conversation>();
    
    public ConversationView() {
        super(new GridLayout(1, 1));
        //Make color map
        colorMap.put("red", Color.red);
        colorMap.put("orange", Color.orange);
        colorMap.put("yellow", Color.yellow);
        colorMap.put("green", Color.green);
        colorMap.put("blue", Color.blue);
        colorMap.put("pink", Color.pink);
        
        setName("Hermes Messenger");
        setPreferredSize(new Dimension(600,400));
        
        //MENU
        menuBar = new JMenuBar();
        file = new JMenu("File");
        menuBar.add(file);
        
        logout = new JMenuItem("Logout");
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.quit();
            }
        });
        file.add(logout);
        
        //TABBY
        tabby = new JTabbedPane();
        
        //NEW CONVO PANEL        
        JComponent newPanel = newConvoPanel();
        tabby.addTab("New", newPanel);
        
        //CONERSATION PANELS
        historyModel = new DefaultListModel();
        history = new JList(historyModel);
        history.setLayoutOrientation(JList.VERTICAL);
        historyScroll = new JScrollPane(history); 
        
        for (String convoID : User.getMyConvos().keySet()) {
            Conversation convo = User.getMyConvos().get(convoID);
            JComponent panel = makePanel(convo);
            tabby.addTab(parseConvoID(convoID), panel);
            JLabel cid = new JLabel(parseConvoID(convoID));
            cid.setName(convoID);
            tabby.setTabComponentAt(tabby.getTabCount()-1, cid);
            tabby.setBackgroundAt(tabby.getTabCount()-1, getColorforConvo(convo));
            tabMap.put(tabby.getTabCount()-1, convo);
        }
        add(tabby);
        tabby.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    /**
     * Parses convoID to remove own name for display on tab
     * @param convoID
     * @return new name
     */
    private static String parseConvoID(String convoID) {
        StringBuilder sb = new StringBuilder();
        for (String un:convoID.split(" ")) {
            if (!un.equals(User.getUsername())) {
                sb.append(un);
                sb.append(" ");
            }
        } return sb.toString();
    }
    
    /**
     * Returns the appropriate color for a conversation based on user colors
     * @param convo) the conversation
     * @return the color to use
     */
    private static Color getColorforConvo(Conversation convo) {
        String firstUN = parseConvoID(convo.getConvoID()).split(" ")[0];
        ConcurrentHashMap<String, UserInfo> users = User.getOnlineUsers();
        return colorMap.get(users.get(firstUN).getColor());
    }
    
    /**
     * Makes a panel for a conversation
     * @param convo, the conversation associated with that tab
     * @return the panel
     */
    private static JComponent makePanel(Conversation convo) {
        fillHistory(convo);
        
        //MESSAGE
        final JTextField message = new JTextField();
        message.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String msg = message.getText();
                if (!msg.equals("")) {
                    message.setText("");
                    Conversation convo = tabMap.get(tabby.getSelectedIndex());
                    User.addMsgToConvo(convo, msg);
                    fillHistory(convo);
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
                    Conversation convo = tabMap.get(tabby.getSelectedIndex());
                    User.addMsgToConvo(convo, msg);
                    fillHistory(convo);
                }
            }
        });
        
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(message, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.LINE_END);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(historyScroll, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.PAGE_END);

        return panel;
    }
    
    /**
     * fills the history with the messages from the input conversation
     * @param convo the conversation to be displayed
     */
    public static void fillHistory(Conversation convo) {
        //Reset the guessTable for the new game
        historyModel.removeAllElements();
        //Fill with conversation history
        for (int i = 0; i < convo.getMessages().size(); i++) {
            Message msg = convo.getMessages().get(i);
            historyModel.addElement(msg.getSender().getUsername() + ": " + msg.getText());
        } tabby.repaint();
    }
    
    /**
     * creates panel for starting new conversations
     * @return the new conversation panel
     */
    private JComponent newConvoPanel() {
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        updateOnlineUsers();
        JScrollPane listScroller = new JScrollPane(list);        
        
        JButton startButton = new JButton();
        startButton.setText("Start Conversation");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Object[] usernames = list.getSelectedValues();
                try {
                    User.startConvo(usernames);
                } catch (DuplicateConvoException e) {
                    JOptionPane.showMessageDialog(getRootPane(), "Conversation already exists");
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(listScroller, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.PAGE_END);
        return panel;
    }
    
    /**
     * Updates panel containing all online users
     */
    public static void updateOnlineUsers() {
        listModel.removeAllElements();
        ConcurrentHashMap<String, UserInfo> users = User.getOnlineUsers();
        for (String un: users.keySet()) {
            listModel.addElement(un);
        }
    }
    
    /**
     * Adds tabs for each conversation if they don't already exist
     */
    public static void updateTabs() {
        for (Conversation convo : User.getMyConvos().values()) {
            if (!tabMap.values().contains(convo)) {
                String convoID = convo.getConvoID();
                JComponent panel = makePanel(convo);
                tabby.addTab(parseConvoID(convoID), panel);
                JLabel cid = new JLabel(parseConvoID(convoID));
                cid.setName(convoID);
                tabby.setTabComponentAt(tabby.getTabCount()-1, cid);
                tabby.setBackgroundAt(tabby.getTabCount()-1, getColorforConvo(convo));
                tabMap.put(tabby.getTabCount()-1, convo);
            }
        } tabby.repaint();
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Hermes Messenger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Add content to the window.
        frame.add(new ConversationView(), BorderLayout.CENTER);
        frame.setJMenuBar(menuBar);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
     
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
