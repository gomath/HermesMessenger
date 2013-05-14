package client;

import javax.swing.JFrame;

public class UserGUI  extends JFrame {
    private static LoginView login = null;
    private static ConversationView convo = null;
    private static User user;
    public UserGUI(User user1) {
        user = user1;
        login = new LoginView();
        user.setLoginView(login);
        login.main(new String[]{});
    }
    public static void openConversationView() {
        login.close();
        convo = new ConversationView();
        convo.main(new String[]{});
    }
    public static void openLoginView() {
        convo.close();
        login = new LoginView();
        login.main(new String[]{});
    }
}
