package client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.Format.Field;
import java.util.concurrent.ConcurrentHashMap;

import exceptions.InvalidUsernameException;

public class Client {
    private final UserGUI gui;
    private static User user;
    
    public Client(){
        this.gui = new UserGUI();
    }
    
    public void setUser(String username, Color color, Socket socket){
        user = new User(username, color, socket);
    }
    
    public static void attemptLogin(String IP, String port, String username, String color) throws UnknownHostException, IOException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Socket socket = new Socket(IP, Integer.parseInt(port));
        int i=0;
        for(char c:username.toCharArray()){
            i++;
            if(!Character.isLetter(c) || i>10){
                throw new InvalidUsernameException();
            }
        }
        java.lang.reflect.Field field = Color.class.getField("yellow");
        Color colorObj = (Color)field.get(null);
        user = new User(username, colorObj, socket);
        user.login();
    }
}
