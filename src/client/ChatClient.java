package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import exceptions.InvalidUsernameException;

public class ChatClient {
    private final UserGUI gui;
    private static User user;
    
    public ChatClient(){
        this.gui = new UserGUI();
        GUIThread guiThread = new GUIThread();
        System.out.println("starting");
        new Thread(guiThread).start();
    }
    
    public static void setUser(String username, String color, Socket socket){
        user = new User(username, color, socket);
    }
    
    public static void attemptLogin(String IP, String port, String username, String color) throws UnknownHostException, IOException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Socket socket = new Socket(IP, Integer.parseInt(port));
        System.out.println("socket made " + socket.toString());
        int i=0;
        for(char c:username.toCharArray()){
            i++;
            if(!Character.isLetter(c) || i>10){
                throw new InvalidUsernameException();
            }
        }
        //java.lang.reflect.Field field = Color.class.getField("yellow");
        //Color colorObj = (Color)field.get(null);
        setUser(username, color, socket);
        user.login();
        System.out.println("starting main");
        user.main();
    }
}
