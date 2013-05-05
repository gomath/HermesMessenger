package client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class User {
    private final String username;
    private final Color color;
    private final Socket socket;
    private static ConcurrentHashMap<String, UserInfo> onlineUsers;
    private Conversation activeConvo;
    private static ConcurrentHashMap<String, Conversation> myConvos;
    
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
        handleConnection(new Socket());
    }
    
    /**
     * Handle a single server connection.  Returns when server disconnects.
     * @param socket socket where the user is connected
     * @throws IOException if connection has an error or terminates unexpectedly
     */
    public static void handleConnection(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        try {
            for (String line =in.readLine(); line!=null; line=in.readLine()) {
                handleRequest(line);
            }
        } finally {        
            out.close();
            in.close();
        }
    }
    
    private static void handleRequest(String input) {
        String[] tokens = input.split(" ");
        if (tokens[0].equals("-f")) {
            ConcurrentHashMap<String, UserInfo> map = new ConcurrentHashMap<String, UserInfo>();
            for(int i=1; i<tokens.length; i++){
                if(i%2==0){
                    map.put(tokens[i], new UserInfo(tokens[i],tokens[i+1]));
                }
            }   
            setOnlineUsers(map);
        }
        else if (tokens[0].equals("-o")){
            addOnlineUser(new UserInfo(tokens[1],tokens[2]));
        }
        else if (tokens[0].equals("-q")){
            removeOnlineUser(tokens[1]);
        }
        else if (tokens[0].equals("-s") || tokens[0].equals("-x")){
            String senderName = null;
            Conversation convo = null;
            //StringBuilder convoID = new StringBuilder();   
            ConcurrentHashMap<String, UserInfo> map = new ConcurrentHashMap<String, UserInfo>();
            for(int i=1; i<tokens.length; i++){
                if (tokens[i].equals("-u")){
                   convo = new Conversation(map);
                   senderName = tokens[i+1];
                   break; 
                }
                else{
                   map.put(tokens[i], onlineUsers.get(tokens[i])); 
                }
            }
            if(tokens[0].equals("-x")){removeMyConvo(convo);}
            else{addNewMyConvo(convo);}
        }
        
        // Should never get here--make sure to return in each of the valid cases above.
        throw new UnsupportedOperationException();
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
    private static void setOnlineUsers(ConcurrentHashMap<String, UserInfo> userMap){
        onlineUsers = userMap;
    }
    public static void addOnlineUser(UserInfo user){
        onlineUsers.put(user.getUsername(), user);
    }
    public static void removeOnlineUser(String user){
        for (String username : onlineUsers.keySet()){
            if(user.equals(username)){
                onlineUsers.remove(username);
            }
        }
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
        return onlineUsers;
    }
    public ConcurrentHashMap<String, Conversation> getMyConvos(){
        return myConvos;
    }
    public static void addNewMyConvo(Conversation convo){
        myConvos.put(convo.getConvoID(), convo);
    }
    public static void removeMyConvo(Conversation convo){
        for (String ID: myConvos.keySet()){
            if (ID.equals(convo.getConvoID())){
                myConvos.remove(ID);
            }
        }
    }
    
    
    
}
