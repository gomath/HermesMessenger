package client;

import javax.swing.JFrame;

public class UserGUI  extends JFrame {
    public UserGUI() {
        new LoginView();
    }
    public static void openConversationView() {
        new ConversationView();
    }
    public static void openLoginView() {
        new LoginView();
    }
}
