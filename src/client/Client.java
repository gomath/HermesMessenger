package client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import exceptions.InvalidUsernameException;

public class Client {
    private final UserGUI gui;
    private static User user;
    
    public Client(){
        this.gui = new UserGUI();
    }
    
    public void setUser(String username, Color color, Socket socket){
        this.user = new User(username, color, socket);
    }
    
    public static void attemptLogin(String IP, String port, String username, String color) throws NumberFormatException, UnknownHostException, IOException{
        Socket socket = new Socket(IP, Integer.parseInt(port));
        for(char c:username.toCharArray()){
            if(!Character.isLetter(c)){
                throw new InvalidUsernameException();
            }
        }
        user = new User(username, Color.getColor(color), socket);
        user.login();
    }
}
