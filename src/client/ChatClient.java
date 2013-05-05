package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    
    public static boolean attemptLogin(String IP, String port, String username, String color) throws UnknownHostException, IOException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        System.out.println(username + "attempting login");
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
        /*
        setUser(username, color, socket);
        user.login();
        */
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("-l "+username+" "+color);
        out.flush();
        for (String line =in.readLine(); line!=null; line=in.readLine()) {
            System.out.println("CLIENT INBOX: " + line);
            String [] tokens = line.split(" ");
            if(tokens[0].equals("-i")) {
                in.close();
                out.close();
                return false;
            
            }
            System.out.println("starting main");
            setUser(username, color, socket);
            user.handleRequest(line);
            //user.login()
            user.main();
            return true;
            
        }
        /*
        System.out.println("starting main");
        user.main();
        */
        return false;
    }
}
