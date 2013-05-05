package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.GroupLayout;
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
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import exceptions.InvalidUsernameException;

public class ConversationView extends JPanel{
    private final JTabbedPane tabby;
    private static JMenuBar menuBar;
    private final JMenu file;
    private final JMenuItem newConvo;
    private final JMenuItem logout;
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
        colorMap.put("red", new Color(227, 38, 54));
        colorMap.put("orange", Color.orange);
        colorMap.put("yellow", new Color(255, 216, 0));
        colorMap.put("green", new Color(0, 165, 80));
        colorMap.put("blue", new Color(96, 80, 220));
        colorMap.put("pink", new Color(246, 83, 166));
        
        setName("Hermes Messenger");
        //setBackground(colorMap.get(User.getColor()));
        setPreferredSize(new Dimension(600,400));
        
        //MENU
        menuBar = new JMenuBar();
        file = new JMenu("File");
        menuBar.add(file);
        newConvo = new JMenuItem("New Conversation");
        newConvo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConcurrentHashMap<String, UserInfo> users = User.getOnlineUsers();
                JList list = new JList(users.keySet().toArray());
                list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                list.setLayoutOrientation(JList.VERTICAL);
                list.setVisibleRowCount(10);
                JScrollPane listScroller = new JScrollPane(list);
                JOptionPane.showInputDialog(listScroller, "Select User(s)", "New Conversation");
                add(listScroller);
                //JOptionPane.showMessageDialog(, "Select User(s)");                
            } 
        });
        file.add(newConvo);
        
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
        JComponent newPanel = newConvoPanel();
        tabby.addTab("New", newPanel);
        
        for (String convoID : User.getMyConvos().keySet()) {
            JComponent panel = makePanel(User.getMyConvos().get(convoID));
            tabby.addTab(parseConvoID(convoID), panel);
            JLabel cid = new JLabel(convoID);
            cid.setName(convoID);
            tabby.setTabComponentAt(tabby.getTabCount()-1, cid);
            tabby.setBackgroundAt(tabby.getTabCount()-1, getColorforConvo(convoID));
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
    
    private static Color getColorforConvo(String convoID) {
        String firstUN = parseConvoID(convoID).split(" ")[0];
        ConcurrentHashMap<String, UserInfo> users = User.getOnlineUsers();
        return colorMap.get(users.get(firstUN).getColor());
    }
    
    private JComponent makePanel(Conversation convo) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(parseConvoID(convo.getConvoID()));
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    
    private JComponent newConvoPanel() {
        ConcurrentHashMap<String, UserInfo> users = User.getOnlineUsers();
        final JList list = new JList(users.keySet().toArray());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        //list.setVisibleRowCount(1);
        JScrollPane listScroller = new JScrollPane(list);        
        
        JButton submitButton = new JButton();
        submitButton.setText("Start Conversation");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Object[] usernames = list.getSelectedValues();
                User.startConvo(usernames);
                updateTabs();
            }
        });
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(listScroller, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.PAGE_END);
    
        return panel;
    }
    
    private void updateTabs() {
        for (String convoID : User.getMyConvos().keySet()) {
            boolean there = false;
            System.out.println(tabby.getTabCount());
            for (int i = 0; i < tabby.getTabCount(); i++) {
                Component tab = tabby.getTabComponentAt(i);
                System.out.println("TAB: " + tab);
                if(tab != null) {
                    if (convoID.equals(tab.getName())) {
                        there = true;
                    }
                }
            }
            if (!there) {
                JComponent panel = makePanel(User.getMyConvos().get(convoID));
                tabby.addTab(parseConvoID(convoID), panel);
                JLabel cid = new JLabel(convoID);
                cid.setName(convoID);
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
