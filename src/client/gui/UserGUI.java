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
        this.login = new LoginView(this);
        System.out.println(this.login + "end");
        //user.setLoginView(this.login);
        this.login.main(new String[]{});
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
        convo.close();
        this.login = new LoginView(this);
        client.getUser().setLoginView(this.login);
    }

    /**
     * Get the GUI's User
     * @return the User
     */
    public User getUser(){
        return client.getUser();
    }
    /**
     * Set the User's LoginView
     */
    public void setUserView() {
        client.getUser().setLoginView(this.login);
    }
    public void setConvoView(ConversationView view) {
        convo = view;
    }
    public ChatClient getClient() {
        return client;
    }
}
