package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ConversationView extends JPanel{
    private final JTabbedPane tabby;
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

        setName("Hermes Messenger");
        setBackground(new Color(96, 80, 220));
        setPreferredSize(new Dimension(600,400));
        //TABBY
        tabby = new JTabbedPane();
        for (String convoID : User.getMyConvos().keySet()) {
            JComponent panel1 = makePanel(User.getMyConvos().get(convoID));
            tabby.addTab("parseConvoID(convoID)", panel1);
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
    
    public JComponent makePanel(Conversation convo) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(parseConvoID(convo.getConvoID()));
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ConversationView");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        //Add content to the window.
        frame.add(new ConversationView(), BorderLayout.CENTER);
         
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
