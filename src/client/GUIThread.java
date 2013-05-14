package client;


public class GUIThread implements Runnable {
    private static User user;
    
    public GUIThread(User user1){
        user = user1;
    }
    
    public void run(){
        new UserGUI(user);
    }
    
}