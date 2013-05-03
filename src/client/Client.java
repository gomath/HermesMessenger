package client;

import java.awt.Color;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Client {
    private final UserGUI gui;
    private User user;
    
    public Client(){
        this.gui = new UserGUI();
    }
    
    public void setUser(String username, Color color, Socket socket){
        this.user = new User(username, color, socket);
    }
}
