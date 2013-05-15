package client.gui;

import javax.swing.JFrame;

import client.user.User;
/**
 * initializes the gui, and manages transtitions between
 * the login and conversation views
 *
 */
public class UserGUI  extends JFrame {
    private static final long serialVersionUID = 1L;
    private static LoginView login = null;
    private static ConversationView convo = null;
    
    /**
     * initializes the login view 
     */
    public UserGUI() {
        login = new LoginView();
        User.setLoginView(login);
        login.main(new String[]{});
    }
    
    /**
     * called to close the login view and start conversation view
     */
    public static void openConversationView() {
        login.close();
        convo = new ConversationView();
        convo.main(new String[]{});
    }
    
    /**
     * called to close conversation view and reopen the login view
     */
    public static void openLoginView() {
        convo.close();
        login = new LoginView();
        login.main(new String[]{});
    }
}
