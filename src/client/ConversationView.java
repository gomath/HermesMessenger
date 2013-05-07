package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class ConversationView extends JPanel{
    private static JTabbedPane tabby;
    private static JMenuBar menuBar;
    private final JMenu file;
    private final JMenuItem logout;
    private static DefaultListModel listModel;
    private static DefaultListModel historyModel;
    private static JList history;
    private static JList list;
    private final static ConcurrentHashMap<String, Color> colorMap = new ConcurrentHashMap<String, Color>();
    /*
    private final JScrollPane scrolly;
    private final JTable messages;
    private final JButton logout;
    private final JLabel newConvoLabel;
    private final JComboBox userlist;
    private final JButton newConvoButton;
    */
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
        //setBackground(colorMap.get(User.getColor()));
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
        
        //CONVO PANEL        
        JComponent newPanel = newConvoPanel();
        tabby.addTab("New", newPanel);
        
        for (String convoID : User.getMyConvos().keySet()) {
            JComponent panel = makePanel(User.getMyConvos().get(convoID));
            tabby.addTab(parseConvoID(convoID), panel);
            JLabel cid = new JLabel(parseConvoID(convoID));
            cid.setName(parseConvoID(convoID));
            tabby.setTabComponentAt(tabby.getTabCount()-1, cid);
            //tabby.setBackgroundAt(tabby.getTabCount()-1, getColorforConvo(convoID));
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
     * Unparses convoID to add own name
     * @param convoID without yourself (as displayed in gui)
     * @return new name
     */
    private static String unParseConvoID(String convoIDnoMe) {
        ArrayList<String> uns = new ArrayList<String>();
        for (String un:convoIDnoMe.split(" ")) {
            uns.add(un);
        } uns.add(User.getUsername());
        Collections.sort(uns);
        StringBuilder sb = new StringBuilder();
        for (String un : uns) {
            sb.append(un);
            sb.append(" ");
        } return sb.toString();
    }
    
    private static Color getColorforConvo(String convoID) {
        String firstUN = parseConvoID(convoID).split(" ")[0];
        ConcurrentHashMap<String, UserInfo> users = User.getOnlineUsers();
        return colorMap.get(users.get(firstUN).getColor());
    }
    
    private static JComponent makePanel(Conversation convo) {
        historyModel = new DefaultListModel();
        //HISTORY
        history = new JList(historyModel);
        history.setLayoutOrientation(JList.VERTICAL);
        //history.setBackground(getColorforConvo(convo.getConvoID()));
        fillHistory(convo.getConvoID());
        
        //MESSAGE
        final JTextField message = new JTextField();
        message.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String msg = message.getText();
                if (!msg.equals("")) {
                    message.setText("");
                    String convoIDnoMe = tabby.getTabComponentAt(tabby.getSelectedIndex()).getName();
                    String convoID = unParseConvoID(convoIDnoMe);
                    User.addMsgToConvo(User.getMyConvos().get(convoID), msg);
                    fillHistory(unParseConvoID(convoIDnoMe));
                }
            }
        });
        
        //SEND BUTTON
        JButton submitButton = new JButton();
        submitButton.setText("Send");
        //getRootPane().setDefaultButton(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String msg = message.getText();
                if (!msg.equals("")) {
                    message.setText("");
                    String convoIDnoMe = tabby.getTabComponentAt(tabby.getSelectedIndex()).getName();
                    String convoID = unParseConvoID(convoIDnoMe);
                    User.addMsgToConvo(User.getMyConvos().get(convoID), msg);
                    fillHistory(unParseConvoID(convoIDnoMe));
                }
            }
        });
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(message, BorderLayout.CENTER);
        messagePanel.add(submitButton, BorderLayout.LINE_END);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(history, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.PAGE_END);
    
        return panel;
    }
    
    public static void fillHistory(String convoID) {
        //Reset the guessTable for the new game
        int rows = historyModel.getSize();
        for (int i = rows-1; i >= 0; i--) {
            historyModel.remove(i); 
        }
        Conversation convo = User.getMyConvos().get(convoID);
        for (int i = 0; i < convo.getMessages().size(); i++) {
            Message msg = convo.getMessages().get(i);
            historyModel.addElement(msg.getSender().getUsername() + ": " + msg.getText());
            history.setForeground(colorMap.get(msg.getSender().getColor()));
        }
    }
    
    private JComponent newConvoPanel() {
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        updateOnlineUsers();
        
        //list.setVisibleRowCount(1);
        JScrollPane listScroller = new JScrollPane(list);        
        
        JButton submitButton = new JButton();
        submitButton.setText("Start Conversation");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Object[] usernames = list.getSelectedValues();
                User.startConvo(usernames);
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(listScroller, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.PAGE_END);
        return panel;
    }
    
    public static void updateOnlineUsers() {
        int rows = listModel.getSize();
        for (int i = rows-1; i >= 0; i--) {
            listModel.remove(i); 
        }
        ConcurrentHashMap<String, UserInfo> users = User.getOnlineUsers();
        for (String un: users.keySet()) {
            listModel.addElement(un);
        }
    }
    
    public static void updateTabs() {
        for (String convoID : User.getMyConvos().keySet()) {
            boolean there = false;
            for (int i = 0; i < tabby.getTabCount(); i++) {
                Component tab = tabby.getTabComponentAt(i);
                if(tab != null) {
                    if (convoID.equals(tab.getName())) {
                        there = true;
                    }
                }
            }
            if (!there) {
                System.out.println("ADDING TAB: " + convoID);
                JComponent panel = makePanel(User.getMyConvos().get(convoID));
                tabby.addTab(parseConvoID(convoID), panel);
                JLabel cid = new JLabel(parseConvoID(convoID));
                cid.setName(parseConvoID(convoID));
                tabby.setTabComponentAt(tabby.getTabCount()-1, cid);
                tabby.setBackgroundAt(tabby.getTabCount()-1, getColorforConvo(convoID));
            }
        }
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
