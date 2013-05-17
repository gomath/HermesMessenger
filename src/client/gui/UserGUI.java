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
    private final ChatClient client;
    
    /**
     * initializes the login view 
     */
    public UserGUI(ChatClient client) {
        this.client = client;
        //this.login = new LoginView(this);
        LoginView.main(this);
    }
    
    /**
     * called to close the login view and start conversation view
     */
    public void openConversationView() {
        this.login.close();
        ConversationView.main(this);
    }
    
    /**
     * called to close conversation view and reopen the login view
     */
    public void openLoginView() {
        System.out.println("I'm closing stuff haha");
        convo.close();
        this.login = new LoginView(this);
        client.getUser().setLoginView(this.login);
    }


    /**
     * Set the User's LoginView to be the current one
     */
    public void setUserView() {
        client.getUser().setLoginView(this.login);
    }
    
    /**
     * Set convo to be view, this is called once a Client
     * successfully logs on
     * @param view the ConversationView to set it to
     */
    public void setConvoView(ConversationView view) {
        convo = view;
    }
    /**
     * Set login to be view
     * @param view the LoginView to set it to
     */
    public void setLoginView(LoginView view) {
        login = view;
    }
    
    /**
     * Get the GUI's Client
     * @return the client
     */
    public ChatClient getClient() {
        return client;
    }
    
    /**
     * Get the GUI's User
     * @return the User
     */
    public User getUser(){
        return client.getUser();
    }
}
