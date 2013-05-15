package client.gui;

import javax.swing.JFrame;

import client.ChatClient;
import client.user.User;
/**
 * initializes the gui, and manages transitions between
 * the login and conversation views
 *
 */
public class UserGUI  extends JFrame {
    private static final long serialVersionUID = 1L;
    private LoginView login = null;
    private ConversationView convo = null;
    private User user;
    private final ChatClient client;
    
    /**
     * initializes the login view 
     */
    public UserGUI(User user, ChatClient client) {
        this.user = user;
        this.client = client;
        this.login = new LoginView(this.client, this);
        System.out.println(this.login + "end");
        //user.setLoginView(this.login);
        this.login.main(new String[]{});
    }
    
    /**
     * called to close the login view and start conversation view
     */
    public void openConversationView() {
        this.login.close();
        //this.convo = new ConversationView(user, this);
        ConversationView.main(this.user, this);
    }
    
    /**
     * called to close conversation view and reopen the login view
     */
    public void openLoginView() {
        ConversationView.close();
        this.login = new LoginView(this.client, this);
        user.setLoginView(this.login);
        //login.main(new String[]{});
    }
    public void setUser(User user1) {
        user = user1;
    }
    public void setUserView() {
        user.setLoginView(this.login);
    }
    public void setConvoView(ConversationView view) {
        convo = view;
    }
}
