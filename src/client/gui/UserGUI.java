package client.gui;

import javax.swing.JFrame;

import client.user.User;

public class UserGUI  extends JFrame {
    private static final long serialVersionUID = 1L;
    private static LoginView login = null;
    private static ConversationView convo = null;
    private static User user;
    public UserGUI(User user1) {
        user = user1;
        System.out.println("haven't made login!" + Thread.currentThread().getId());
        login = new LoginView();
        System.out.println("just made login!");
        User.setLoginView(login);
        System.out.println("starting login.main");
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
