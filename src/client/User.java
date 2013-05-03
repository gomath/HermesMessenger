package client;

import java.awt.Color;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private final String username;
    private final Color color;
    private final Socket socket;
    private ConcurrentHashMap<String, UserInfo> onlineUsers;
    private Conversation activeConvo;
    private ConcurrentHashMap<String, Conversation> myConvos;
    
    public User(String username, Color color, Socket socket){
        this.username = username;
        this.color = color;
        this.socket = socket;
        this.onlineUsers = new ConcurrentHashMap<String, UserInfo>();
        this.myConvos = new ConcurrentHashMap<String, Conversation>();
    }
    
    
}
