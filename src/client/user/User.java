package client.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import client.ClientThread;
import client.gui.ConversationView;
import client.gui.LoginView;
import client.gui.UserGUI;

import exceptions.DuplicateConvoException;

public class User {
    private String username;
    private String color;
    private Socket socket;
    private UserGUI gui;
    private LoginView loginView;
    private ConversationView convoView;
    private ConcurrentHashMap<String, UserInfo> onlineUsers;
    private ConcurrentHashMap<String, Conversation> myConvos;
    private ConcurrentHashMap<String, Conversation> inactiveConvos;
    private PrintWriter out;
    
    
    /**
     * Create a User, initialize its instance variables
     * @param username a String representing the user's unique screenname
     * @param color a Color representing the User's color preference
     * @param socket a Socket that corresponds to the User's connection
     */
    public User(String username1, String color1, Socket socket1){
        username = username1;
        color = color1;
        socket = socket1;
        onlineUsers = new ConcurrentHashMap<String, UserInfo>();
        myConvos = new ConcurrentHashMap<String, Conversation>();
        inactiveConvos = new ConcurrentHashMap<String, Conversation>();
        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    
    /**
     * Handle a single server connection.  Returns when server disconnects.
     * @param socket Socket where the user is connected
     * @throws IOException if connection has an error or terminates unexpectedly
     */
    public void handleConnection(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        try {
            //read from the socket
            for (String line =in.readLine(); line!=null; line=in.readLine()) {
                if (line.length() != 0) {
                    handleRequest(line);
                }
            }
        } finally {        
            out.close();
            in.close();
        }
    }
    /**
     * Handles requests from the server, according to the grammar
     * @param input, the received message from the server
     */
    public void handleRequest(String input) {
        String[] tokens = input.split(" ");
        if (input.length()==0){;} //ignore empty strings
        //when the message is the list of online users
        else if (tokens[0].equals("-f")) {
            //login was successful, so open the ConversationView
            Runnable openConvoView = new Runnable() {
                public void run(){
                    gui.openConversationView();
                }
            };
            SwingUtilities.invokeLater(openConvoView); //add to Event Queue
            ConcurrentHashMap<String, UserInfo> map = new ConcurrentHashMap<String, UserInfo>();
            
            //add each other User to this user's onlineusers map
            for(int i=1; i<tokens.length; i++){
                if(i%2==1){
                    map.put(tokens[i], new UserInfo(tokens[i],tokens[i+1]));
                }
            }   
            setOnlineUsers(map);
        }
        //a new User has logged on
        else if (tokens[0].equals("-o")){
            //add the user
            addOnlineUser(new UserInfo(tokens[1],tokens[2]));
            //update on the GUI
            Runnable update = new Runnable() {
                public void run(){
                    convoView.updateOnlineUsers();
                }
            };
            SwingUtilities.invokeLater(update);
        }
        //if a user has logged out
        else if (tokens[0].equals("-q")){
            //remove the user
            removeOnlineUser(tokens[1]);
            //delete all of their conversation history
            for (String convoID : inactiveConvos.keySet()) {
                if (convoID.contains(tokens[1])) {
                    inactiveConvos.remove(convoID);
                }
            }
            //update the list on the gui
            Runnable update = new Runnable() {
                public void run(){
                    convoView.updateOnlineUsers();
                }
            };
            SwingUtilities.invokeLater(update);
        }
        //to add or delete a conversation
        else if (tokens[0].equals("-s") || tokens[0].equals("-x")){
            ConcurrentHashMap<String, UserInfo> map = new ConcurrentHashMap<String, UserInfo>();
            Conversation convo = null;
            //build the hashmap of usernames to UserInfo
            for(int i=1; i<tokens.length; i++){
                //when the sender name is found, make the conversation
                if (tokens[i].equals("-u")){
                   convo = new Conversation(map);
                   break; 
                }
                //for each user, find their UserInfo from the onlineUsers map,
                //and add it to convo's participants map
                else{
                   if (tokens[i].equals(this.username)) {
                       map.put(tokens[i], new UserInfo(this.username, color)); 
                   } else {
                       map.put(tokens[i], onlineUsers.get(tokens[i])); 
                   }
                }
            }
            //add or remove the convo
            if(tokens[0].equals("-x")){removeMyConvo(convo);}
            else{addNewMyConvo(convo);} //if it is "-s"
        }
        //if the username is invalid
        else if(tokens[0].equals("-i")){ 
            //popup a GUI message saying the username is duplicate
            Runnable duplicateUN = new Runnable() {
                public void run(){
                    JOptionPane.showMessageDialog(loginView.getContentPane(), "Username already in use");
                }
            };
            SwingUtilities.invokeLater(duplicateUN);
        }
        //if there is a new message, just call updateConvo
        else if(tokens[0].equals("-c")){
            updateConvo(input);
        }
        
        // Should never get here
        else{
            throw new UnsupportedOperationException();
        }
    }
    
    /**
     * Parse an "update" server message, updates conversation with new message
     * @param input, message from the server (-c .... -u ... -t ... ) according to grammar
     */
    public void updateConvo(String input) {
        //Parse the message data into appropriate fields
        boolean convo_id = false;
        StringBuilder ci = new StringBuilder();
        boolean user = false;
        StringBuilder un = new StringBuilder();
        boolean text = false;
        StringBuilder msg = new StringBuilder();
        for (String token: input.split(" ")) {
            //builds the convo_id using tokens before the -u
            if (convo_id && !token.equals("-u")) {
                ci.append(token);
                ci.append(" ");
            //builds the username using the token before the -t
            } else if (user && !token.equals("-t")) {
                un.append(token);
            //otherwise it must be part of the text
            } else if (text) {
                msg.append(token);
                msg.append(" ");
            }
            //after the -c, start looking for the convo_id
            if (token.equals("-c")) {
                convo_id = true;
            //after the -u, start looking for username instead
            } else if (token.equals("-u")) {
                convo_id = false;
                user = true;
            //after the -t, start looking for the text instead
            } else if (token.equals("-t")) {
                user = false;
                text = true;
            }
        }
        //add the message to the convo that has the correct convo_id
        Conversation convo = myConvos.get(ci.toString());
        convo.addMessage(new Message(onlineUsers.get(un.toString()), convo, msg.toString()));
        
        
        //update tab from GUI thread
        final class updateRunnable implements Runnable {
            private Conversation convo;
            
            public updateRunnable(Conversation convo1){
                convo = convo1;
            }
            public void run(){
                convoView.updateTab(convo.getConvoID());
            }  
        }
        Runnable update = new updateRunnable(convo);
        SwingUtilities.invokeLater(update);
    }
    
    /**
     * Tell the server to start a new conversation, according to the grammar,
     * and add the new convo to this User's convo map and GUI
     * @param convo the Conversation to start
     */
    public String startConvo(Object[] usernames){
        ConcurrentHashMap<String, UserInfo> participants = new ConcurrentHashMap<String, UserInfo>();
        //make a participants list for this conversation
        for (Object un: usernames) {
            participants.put((String) un, onlineUsers.get(un));
        }
        participants.put(this.username, new UserInfo(this.username, color));
        Conversation convo = new Conversation(participants);
        //add to User's convo list
        addNewMyConvo(convo);
        //tell server to tell other Users about the new convo
        return sendMessageToServer("-s " + convo.getConvoID() + "-u " + username);
    }
    
    /**
     * Tell the server to end a conversation, according to the grammar,
     * and remove the convo from this User's convo map and GUI
     * @param convo the Conversation to end
     */
    public String closeConvo(Conversation convo){
        removeMyConvo(convo);
        return sendMessageToServer("-x " + convo.getConvoID() + "-u " + username);
    }
    
    /**
     * Tell the server to add a message to a Conversation, according to the grammar,
     * and add it to the User's Conversation
     * @param convo the Conversation to add the message to
     * @param text the Message
     */
    public String addMsgToConvo(Conversation convo, String text){
        //add the message to this user's conversation
        convo.addMessage(new Message(new UserInfo(this.username, color), convo, text));
        //send message to server to add to OTHER users' conversations
        return sendMessageToServer("-c " + convo.getConvoID() + "-u " + 
                username + " -t " + text);
    }
    
    /**
     * Tell the server to connect the user, according to the grammar
     * @return login message
     */
    public String login(){
        sendMessageToServer("-l " + username + " " + color.toString());
        return "";
    }
    
    /**
     * Tell the server to disconnect the user, according to the grammar
     */
    public void quit(){
        //open the login view (which will close the ConversationView)
        gui.openLoginView();
        //close the conversations
        for (Conversation convo : myConvos.values()) {
            closeConvo(convo);
        }
        //tell the server you've given up on life and want to die
        sendMessageToServer("-q " + username);
    }
    
    /**
     * Actually send a String to the server
     * @param text the String to send
     */
    public String sendMessageToServer(String text){
        //send the text to the server
        out.print(text + '\n'); 
        out.flush();
        return text;
    }
    
    /**
     * Methods to completely replace the onlineUsers map, add one user to
     * it, or remove one user from it.
     */
    private void setOnlineUsers(ConcurrentHashMap<String, UserInfo> userMap){
        onlineUsers = userMap;
    }
    public void addOnlineUser(UserInfo user){
        onlineUsers.put(user.getUsername(), user);
    }
    public void removeOnlineUser(String user){
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
        return username;
    }
    public String getColor(){
        return color;
    }
    public Socket getSocket(){
        return socket;
    }
    public ConcurrentHashMap<String, UserInfo> getOnlineUsers(){
        return onlineUsers;
    }
    public ConcurrentHashMap<String, Conversation> getMyConvos(){
        return myConvos;
    }
    public ConcurrentHashMap<String, Conversation> getInactiveConvos(){
        return inactiveConvos;
    }
    
    /**
     * adds a newly created conversation, using an inactiveConvo if 
     * it was already previously created
     * @param convo, the new conversation to be added
     */
    public void addNewMyConvo(Conversation convo){
        checkDuplicateConvo(convo.getConvoID()); //don't allow duplicate conversations
        if (inactiveConvos.keySet().contains(convo.getConvoID())) {
            myConvos.put(convo.getConvoID(), inactiveConvos.get(convo.getConvoID())); //add the inactive convo
            inactiveConvos.remove(convo.getConvoID()); //no longer inactive
        } else {
            myConvos.put(convo.getConvoID(), convo);
        }
        //update tab from GUI thread
        //will already be in event handling thread so no need to make a runnable
        convoView.updateTabs();
        ConversationView.fillHistory(convo.getConvoID());   
    }
    
    /**
     * removes conversation when it has been closed
     * @param convo the conversation to be removed
     */
    public void removeMyConvo(Conversation convo){
        final class updateRunnable implements Runnable {
            private Conversation convo;
            
            public updateRunnable(Conversation convo1){
                convo = convo1;
            }
            public void run(){
                ConversationView.removeTab(convo.getConvoID());
            }  
        }
        Runnable update = new updateRunnable(convo);
        SwingUtilities.invokeLater(update);
        
        //make the convo inactive
        inactiveConvos.put(convo.getConvoID(), myConvos.get(convo.getConvoID()));
        myConvos.remove(convo.getConvoID());
    }
    
    /**
     * checks conversation id to make sure duplicates aren't created
     * @param convoID the potential new conversation id
     */
    public void checkDuplicateConvo(String convoID){
        for (String ID: myConvos.keySet()){
            if(convoID.equals(ID)){
                throw new DuplicateConvoException();
            }
        }
    }
    
    /**
     * sets login view
     * @param login the LoginView
     */
    public void setLoginView(LoginView login){
        loginView = login;
    }
    /**
     * sets conversation view
     * @param convo the ConversationView
     */
    public void setConversationView(ConversationView convo){
        convoView = convo;
    }
    /**
     * sets the GUI
     * @param gui1 the GUI
     */
    public void setGUI(UserGUI gui1){
        gui = gui1;
    }
    
    /**
     * Run a client for the User, on a new thread.
     * @throws IOException if the socket is invalid (should not happen)
     */
    public void main() throws IOException{
        ClientThread clientThread = new ClientThread(this.socket, this);
        new Thread(clientThread).start();
    }
}
