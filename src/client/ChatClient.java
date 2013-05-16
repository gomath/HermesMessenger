package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import client.gui.UserGUI;
import client.user.User;

import exceptions.InvalidUsernameException;

/**
 * Wraps together the GUI and User classes. Starts a GUI and logs a user in.
 */
public class ChatClient {
    private User user;
    
    /**
     * Starts a new UserGUI on the Event thread
     */
    public ChatClient(){
        final class startGui implements Runnable {
            private ChatClient client;
            
            public startGui(ChatClient client){
                this.client = client;
            }
            public void run(){
                new UserGUI(client);
            }  
        }
        Runnable start = new startGui(this);
        SwingUtilities.invokeLater(start);  //put it on event handling thread

    }
    /**
     * Sets the user instance variable, based on the desired username, color and socket
     * @param username a String that is the user's username
     * @param color the desired color (as a String) for the user's GUI theme
     * @param socket the Socket object it will use to communicate with the server
     */
    public void setUser(String username, String color, Socket socket){
        this.user = new User(username, color, socket);
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
    public void attemptLogin(String IP, String port, String username, String color, UserGUI gui) throws UnknownHostException, IOException{
        int i=0;
        //check that each character in the username is a letter
        for(char c:username.toCharArray()){
            i++;
            if(!Character.isLetter(c) || i>10){
                throw new InvalidUsernameException();
            }
        }
        //connect if no errors
        Socket socket = new Socket(IP, Integer.parseInt(port));
        
        //if no errors thrown, set the user attribute of the ChatClient
        this.setUser(username, color, socket);
        user.setGUI(gui);
        
                
    }
    public User getUser(){
        return this.user;
    }
    public void runUser() throws IOException{
        //run main to accept messages
        this.user.main();
        //send a login message
        this.user.login();
    }
    
    public static void main(String[] args){
        new ChatClient();
    }
}
