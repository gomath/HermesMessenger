package client;

import javax.swing.JFrame;

public class UserGUI  extends JFrame {

    public UserGUI() {
        LoginView.main(new String[]{});
    }
    public static void openConversationView() {
        ConversationView.main(new String[]{});
    }
    public static void openLoginView() {
        LoginView.main(new String[]{});
    }
}
