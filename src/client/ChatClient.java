package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import exceptions.InvalidUsernameException;

/**
 * Wraps together the GUI and User classes. Starts a GUI and logs a user in.
 */
public class ChatClient {
    private static User user;
    
    /**
     * Runs a GUIThread
     */
    public ChatClient(){
        Runnable startGui = new Runnable() {
            public void run(){
                System.out.println(Thread.currentThread().getId());
                new UserGUI(user);
            }
        };
        SwingUtilities.invokeLater(startGui);
        //GUIThread guiThread = new GUIThread(user);
        //new Thread(guiThread).start();
    }
    /**
     * Sets the user instance variable, based on the desired username, color and socket
     * @param username a String that is the user's username
     * @param color the desired color (as a String) for the user's GUI theme
     * @param socket the Socket object it will use to communicate with the server
     */
    public static void setUser(String username, String color, Socket socket){
        user = new User(username, color, socket);
    }
    
    /**
     * attempts to log in the user, will throw exceptions based on potential problems.
     * @param IP the String IP address the server should be located at
     * @param port the String port, which should be a number, to connect to at the server's IP
     * @param username the String desired username for the client
     * @param color the String representing the chosen color from the GUI dropdown
     * @throws UnknownHostException - for invalid IP address
     * @throws IOException - for problems with the Socket
     * @throws InvalidUsernameException - when the username is not <= 10 characters and only a-zA-Z
     * @throws NumberFormatException - when the port String is not actually a valid port number,
     * or if it's outside the specified range of valid port values, which is between 0 and 65535, inclusive
     */
    public static void attemptLogin(String IP, String port, String username, String color) throws UnknownHostException, IOException{
        Socket socket = new Socket(IP, Integer.parseInt(port));
        int i=0;
        //check that each character in the username is a letter
        for(char c:username.toCharArray()){
            i++;
            if(!Character.isLetter(c) || i>10){
                throw new InvalidUsernameException();
            }
        }
        //set the user attribute of the ChatClient
        setUser(username, color, socket);
        
        //run main to accept messages
        user.main();
        //send a login message
        User.login();        
    }
    
    public static void main(String[] args){
        new ChatClient();
    }
}
