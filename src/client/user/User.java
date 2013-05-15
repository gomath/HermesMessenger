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
    private static String username;
    private static String color;
    private static Socket socket;
    private static ConcurrentHashMap<String, UserInfo> onlineUsers;
    private static ConcurrentHashMap<String, Conversation> myConvos;
    private static ConcurrentHashMap<String, Conversation> inactiveConvos;
    private static PrintWriter out;
    private static LoginView loginView;
    
    
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
    
    public void main() throws IOException{
        ClientThread clientThread = new ClientThread(socket);
        new Thread(clientThread).start();
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
                if (line.length() != 0) {
                    System.out.println("CLIENT INBOX: " + line);
                    handleRequest(line);
                }
            }
        } finally {        
            out.close();
            in.close();
        }
    }
    
    /**
     * handles requests from the server
     * @param input, the received message from the server
     */
    static void handleRequest(String input) {
        String[] tokens = input.split(" ");
        if (input.length()==0){
            ;
        }
        else if (tokens[0].equals("-f")) {
            Runnable openConvoView = new Runnable() {
                public void run(){
                    System.out.println(Thread.currentThread().getId());
                    UserGUI.openConversationView();
                }
            };
            SwingUtilities.invokeLater(openConvoView);
            ConcurrentHashMap<String, UserInfo> map = new ConcurrentHashMap<String, UserInfo>();
            for(int i=1; i<tokens.length; i++){
                if(i%2==1){
                    map.put(tokens[i], new UserInfo(tokens[i],tokens[i+1]));
                }
            }   
            setOnlineUsers(map);
        }
        else if (tokens[0].equals("-o")){
            addOnlineUser(new UserInfo(tokens[1],tokens[2]));
            Runnable update = new Runnable() {
                public void run(){
                    System.out.println(Thread.currentThread().getId());
                    ConversationView.updateOnlineUsers();
                }
            };
            SwingUtilities.invokeLater(update);
        }
        else if (tokens[0].equals("-q")){
            removeOnlineUser(tokens[1]);
            for (String convoID : inactiveConvos.keySet()) {
                if (convoID.contains(tokens[1])) {
                    inactiveConvos.remove(convoID);
                }
            }
            Runnable update = new Runnable() {
                public void run(){
                    System.out.println(Thread.currentThread().getId());
                    ConversationView.updateOnlineUsers();
                }
            };
            SwingUtilities.invokeLater(update);
        }
        else if (tokens[0].equals("-s") || tokens[0].equals("-x")){
            //String senderName = null;
            Conversation convo = null;
            ConcurrentHashMap<String, UserInfo> map = new ConcurrentHashMap<String, UserInfo>();
            //build the hashmap of usernames to UserInfo
            for(int i=1; i<tokens.length; i++){
                if (tokens[i].equals("-u")){
                   convo = new Conversation(map);
                   //senderName = tokens[i+1];
                   break; 
                }
                else{
                   if (tokens[i].equals(username)) {
                       map.put(tokens[i], new UserInfo(username, color)); 
                   } else {
                       map.put(tokens[i], onlineUsers.get(tokens[i])); 
                   }
                }
            }
            if(tokens[0].equals("-x")){removeMyConvo(convo);}
            else{addNewMyConvo(convo);}
        }
        
        else if(tokens[0].equals("-i")){        
            Runnable duplicateUN = new Runnable() {
                public void run(){
                    System.out.println(Thread.currentThread().getId());
                    JOptionPane.showMessageDialog(loginView.getContentPane(), "Username already in use");
                }
            };
            SwingUtilities.invokeLater(duplicateUN);
        }
        
        else if(tokens[0].equals("-c")){
            updateConvo(input);
        }
        
        // Should never get here
        else{
            throw new UnsupportedOperationException();
        }
    }
    
    /**
     * Actually send a String to the server
     * @param text the String to send
     */
    public static String sendMessageToServer(String text){
        System.out.println("CLIENT OUTBOX: " + text);
        out.print(text + '\n'); 
        out.flush();
        return text;
    }
    
    /**
     * Tell the server to start a new conversation
     * @param convo the Conversation to start
     */
    public static String startConvo(Object[] usernames){
        ConcurrentHashMap<String, UserInfo> participants = new ConcurrentHashMap<String, UserInfo>();
        for (Object un: usernames) {
            participants.put((String) un, onlineUsers.get(un));
        }
        participants.put(username, new UserInfo(username, color));
        Conversation convo = new Conversation(participants);
        addNewMyConvo(convo);
        return sendMessageToServer("-s " + convo.getConvoID() + "-u " + username);
    }
    
    /**
     * Tell the server to end a conversation
     * @param convo the Conversation to end
     */
    public static String closeConvo(Conversation convo){
        removeMyConvo(convo);
        return sendMessageToServer("-x " + convo.getConvoID() + "-u " + username);
    }
    
    /**
     * Tell the server to add a message to a Conversation
     * @param convo the Conversation to add the message to
     * @param text the Message
     */
    public static String addMsgToConvo(Conversation convo, String text){
        convo.addMessage(new Message(new UserInfo(username, color), convo, text));
        return sendMessageToServer("-c " + convo.getConvoID() + "-u " + 
                username + " -t " + text);
    }
    
    /**
     * updates conversation with new message
     * @param input, message from the server
     */
    public static void updateConvo(String input) {
        //Parse the message data into appropriate fields
        boolean convo_id = false;
        StringBuilder ci = new StringBuilder();
        boolean user = false;
        StringBuilder un = new StringBuilder();
        boolean text = false;
        StringBuilder msg = new StringBuilder();
        for (String token: input.split(" ")) {
            if (convo_id && !token.equals("-u")) {
                ci.append(token);
                ci.append(" ");
            } else if (user && !token.equals("-t")) {
                un.append(token);
            } else if (text) {
                msg.append(token);
                msg.append(" ");
            }
            if (token.equals("-c")) {
                convo_id = true;
            } else if (token.equals("-u")) {
                convo_id = false;
                user = true;
            } else if (token.equals("-t")) {
                user = false;
                text = true;
            }
        }
        Conversation convo = myConvos.get(ci.toString());
        if (!un.toString().equals(username)) {
            convo.addMessage(new Message(onlineUsers.get(un.toString()), convo, msg.toString()));
        } else {
            convo.addMessage(new Message(new UserInfo(username, color), convo, msg.toString()));
        } 
        
        //update tab from GUI thread
        final class updateRunnable implements Runnable {
            private Conversation convo;
            
            public updateRunnable(Conversation convo1){
                convo = convo1;
            }
            public void run(){
                System.out.println(Thread.currentThread().getId());
                ConversationView.updateTab(convo.getConvoID());
            }  
        }
        Runnable update = new updateRunnable(convo);
        SwingUtilities.invokeLater(update);
    }
    
    /**
     * creates login message to send to server
     * @return login message
     */
    public static String login(){
        sendMessageToServer("-l " + username + " " + color.toString());
        return "";
    }
    
    /**
     * Tell the server to disconnect the user
     */
    public static void quit(){
        UserGUI.openLoginView();
        for (Conversation convo : myConvos.values()) {
            closeConvo(convo);
        }
        sendMessageToServer("-q " + username);
    }
    
    /**
     * Setter methods for various private instance variables.
     * @param the object to set the variable to
     */
    public static void setActiveConvo(Conversation convo){
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
    public static String getUsername(){
        return username;
    }
    public static String getColor(){
        return color;
    }
    public static Socket getSocket(){
        return socket;
    }
    public static ConcurrentHashMap<String, UserInfo> getOnlineUsers(){
        return onlineUsers;
    }
    public static ConcurrentHashMap<String, Conversation> getMyConvos(){
        return myConvos;
    }
    public static ConcurrentHashMap<String, Conversation> getInactiveConvos(){
        return inactiveConvos;
    }
    
    /**
     * adds a newly created conversation
     * @param convo, the new conversation to be added
     */
    public static void addNewMyConvo(Conversation convo){
        checkDuplicateConvo(convo.getConvoID());
        if (inactiveConvos.keySet().contains(convo.getConvoID())) {
            myConvos.put(convo.getConvoID(), inactiveConvos.get(convo.getConvoID()));
            inactiveConvos.remove(convo.getConvoID());
        } else {
            myConvos.put(convo.getConvoID(), convo);
        }
        
        //update tab from GUI thread
        final class updateRunnable implements Runnable {
            private Conversation convo;
            
            public updateRunnable(Conversation convo1){
                convo = convo1;
            }
            public void run(){
                System.out.println(Thread.currentThread().getId());
                ConversationView.updateTabs();
                ConversationView.fillHistory(convo.getConvoID());
            }  
        }
        Runnable update = new updateRunnable(convo);
        SwingUtilities.invokeLater(update);      
    }
    
    /**
     * removes conversation when it has been closed
     * @param convo the conversation to be removed
     */
    public static void removeMyConvo(Conversation convo){
        final class updateRunnable implements Runnable {
            private Conversation convo;
            
            public updateRunnable(Conversation convo1){
                convo = convo1;
            }
            public void run(){
                System.out.println(Thread.currentThread().getId());
                ConversationView.removeTab(convo.getConvoID());
            }  
        }
        Runnable update = new updateRunnable(convo);
        SwingUtilities.invokeLater(update);
        
        inactiveConvos.put(convo.getConvoID(), myConvos.get(convo.getConvoID()));
        myConvos.remove(convo.getConvoID());
    }
    
    /**
     * checks conversation id to make sure duplicates aren't created
     * @param convoID the potential new conversation id
     */
    public static void checkDuplicateConvo(String convoID){
        for (String ID: myConvos.keySet()){
            if(convoID.equals(ID)){
                throw new DuplicateConvoException();
            }
        }
    }
    
    /**
     * sets login view
     * @param login the loginview
     */
    public static void setLoginView(LoginView login){
        loginView = login;
    }
    
    
}
