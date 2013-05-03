package client;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private final String username;
    private final Color color;
    private final Socket socket;
    private ConcurrentHashMap<String, UserInfo> onlineUsers;
    private Conversation activeConvo;
    private ConcurrentHashMap<String, Conversation> myConvos;
    
    /**
     * Create a User, initialize its instance variables
     * @param username a String representing the user's unique screenname
     * @param color a Color representing the User's color preference
     * @param socket a Socket that corresponds to the User's connection
     */
    public User(String username, Color color, Socket socket){
        this.username = username;
        this.color = color;
        this.socket = socket;
        this.onlineUsers = new ConcurrentHashMap<String, UserInfo>();
        this.myConvos = new ConcurrentHashMap<String, Conversation>();
    }
    
    public void main() throws IOException{
        handleConnection();
    }
    
    public static void handleConnection() throws IOException{
        ; //TO DO
    }
    
    /**
     * Actually send a String to the server
     * @param text the String to send
     */
    public void sendMessageToServer(String text){
        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(text);
        
    }
    
    /**
     * Tell the server to start a new conversation
     * @param convo the Conversation to start
     */
    public void startConvo(Conversation convo){
        sendMessageToServer("-s " + convo.getConvoID());
    }
    
    /**
     * Tell the server to end a conversation
     * @param convo the Conversation to end
     */
    public void closeConvo(Conversation convo){
        sendMessageToServer("-x " + convo.getConvoID());
    }
    
    /**
     * Tell the server to add a message to a Conversation
     * @param convo the Conversation to add the message to
     * @param text the Message
     */
    public void addMsgToConvo(Conversation convo, String text){
        sendMessageToServer("-c " + convo.getConvoID() + " -u " + 
                this.username + " -t " + text);
    }
    
    /**
     * Tell the server to disconnect the user
     */
    public void quit(){
        sendMessageToServer("-q " + this.username);
    }
    
    /**
     * Setter methods for various private instance variables.
     * @param the object to set the variable to
     */
    public void setActiveConvo(Conversation convo){
        this.activeConvo = convo;
    }
    
    /**
     * Methods to completely replace the onlineUsers map, add one user to
     * it, or remove one user from it.
     */
    public void setOnlineUsers(ConcurrentHashMap<String, UserInfo> userMap){
        this.onlineUsers = userMap;
    }
    public void addOnlineUser(UserInfo user){
        onlineUsers.put(user.getUsername(), user);
    }
    public void removeOnlineUser(UserInfo user){
        onlineUsers.remove(user.getUsername());
    }
    
    /**
     * Getter methods for various private instance variables.
     * @return the instance variable.
     */
    public String getUsername(){
        return this.username;
    }
    public Color getColor(){
        return this.color;
    }
    public Socket getSocket(){
        return this.socket;
    }
    public ConcurrentHashMap<String, UserInfo> getOnlineUsers(){
        return this.onlineUsers;
    }
    public ConcurrentHashMap<String, Conversation> getMyConvos(){
        return this.myConvos;
    }
    
    
}
