package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import client.user.UserInfo;

/**
 * 
 * Runs the Server and maintains a ServerSocket to communicate with all Clients.
 */
public class ChatServer {
    private final ServerSocket serverSocket;
    protected ConcurrentHashMap<String, UserInfo> infoMap = new ConcurrentHashMap<String,UserInfo>();
    /**
     * Makes a ChatServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535.
     * @throws IOException
     */
    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    
    /**
     * Run the server, listening for client connections and handling them.
     * Never returns unless an exception is thrown.
     * @throws IOException if the main server socket is broken
     * (IOExceptions from individual clients do *not* terminate serve()).
     */
    protected void serve() throws IOException{
        while(true) {
            //block until a client connects
            Socket socket = serverSocket.accept();
            new ChatServerThread(socket, this).start();
        }
    }
    
    /**
     * Handle a single client connection. Returns when client disconnects.
     * @param socket socket where the client is connected
     * @throws IOException if connection has an error or terminates unexpectedly
     */
    protected void handleConnection(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = null;
        try {
            for (String line = in.readLine(); line!=null; line = in.readLine()) {
                //start the list of outgoing ServerMessage objects
                ArrayList<ServerMessage> outMessages = handleClientRequest(line, socket);
                for(ServerMessage message: outMessages) {
                    //make a PrintWriter and send appropriate Messages to each recipient,
                    //based on the ServerMessages
                    for (Socket recipient: message.getRecipients()) {
                        out = new PrintWriter(recipient.getOutputStream(), true);
                        synchronized(socket){  //synchronized to avoid message sending race conditions
                            out.println(message.getText());
                            out.flush();
                        }
                    }
                }
            }
        } finally {
            out.close();
            in.close();
        }
    }
    
    /**
     * handler for client input
     * 
     * takes in a string representing a message sent from a client and returns a string
     * response which will be the response to send to the client if applicable
     * @param input, the text from the client
     * @return the ServerMessage objects to send back to client(s)
     * @throws IOException
     */
    protected ArrayList<ServerMessage> handleClientRequest(String input, Socket socket) throws IOException {
        String [] tokens = input.split(" ");
        String flag = tokens[0];
        if(flag.equals("-l")) {
            return addUser(tokens[1], tokens[2], socket);
        } else if(flag.equals("-q")) {
            return logout(tokens[1]);
        } else if(flag.equals("-c")) {
            return updateConvo(input);
        } else if(flag.equals("-s")) {
            return startConvo(input);
        } else if(flag.equals("-x")) {
            return closeConvo(input);
        } else {
            System.out.println("No flag recognized incoming message to server!");
        }
        
        return null;
    }
    
    /**
     * Called when a new user tries to come online. Adds user if username is
     * unique, returns message to send to client
     * @param username the potential username
     * @param color the color
     * @param socket the socket this user is connected by
     * @return Message to return to client:
     * INVALID_USER if username already in use
     * ONLINE_USERS if username is unique
     */
    protected ArrayList<ServerMessage> addUser(String username, String color, Socket socket) {
        ArrayList<ServerMessage> messageList = new ArrayList<ServerMessage>();
        if (infoMap.containsKey(username)) {
            //Username is already used, return INVALID_USER message
            ArrayList<Socket> me = new ArrayList<Socket>();
            me.add(socket);
            messageList.add(new ServerMessage(me, "-i " + username, this));
            return messageList;
        } else {
            //Make new UserInfo
            infoMap.put(username, new UserInfo(username, color, socket));
            //Return ONLINE_USERS message, to be sent to everyone but me
            StringBuilder sb = new StringBuilder();
            sb.append("-f ");
            for (String un: infoMap.keySet()) {
                if(un != username) {
                    sb.append(un);
                    sb.append(" ");
                    sb.append(infoMap.get(un).getColor());
                    sb.append(" ");
                }
            } 
            //add the message of all online users
            messageList.add(new ServerMessage(justMe(username), sb.toString(), this));
            //add the message of a new User logging on (for all Users other than new one)
            String notifyOthers = "-o "+username+" "+color;
            messageList.add(new ServerMessage(everyoneButMe(username), notifyOthers, this));
        }
        return messageList;
    }
    
    /**
     * Called when client logs out
     * @param username the username of the user that logged out
     * @return OFFLINE message
     */
    private ArrayList<ServerMessage> logout(String username) {
        ArrayList<ServerMessage> messageList = new ArrayList<ServerMessage>();
        infoMap.remove(username);  //delete user from infoMap
        //add the quit message for everyone else
        messageList.add(new ServerMessage(everyoneButMe(username), "-q " + username, this));
        return messageList;
    }
    
    /**
     * called when someone sends a new message
     * @param message the ADD_MSG message
     * @return UPDATE message to everyone in convo except for whoever sent the message
     */
    private ArrayList<ServerMessage> updateConvo(String message) {
        ArrayList<ServerMessage> messageList = new ArrayList<ServerMessage>();
        //Parse the message data into appropriate fields
        boolean convo_id = false;
        StringBuilder ci = new StringBuilder();
        boolean user = false;
        StringBuilder un = new StringBuilder();
        boolean text = false;
        StringBuilder msg = new StringBuilder();
        for (String token: message.split(" ")) {
            //find convo_id tokens and make the convo-id
            if (convo_id && !token.equals("-u")) {
                ci.append(token);
                ci.append(" ");
            //find username token
            } else if (user && !token.equals("-t")) {
                un.append(token);
            //find text tokens and make the message String
            } else if (text) {
                msg.append(token);
                msg.append(" ");
            }
            //start looking for convo_id
            if (token.equals("-c")) {
                convo_id = true;
            //start looking for username
            } else if (token.equals("-u")) {
                convo_id = false;
                user = true;
            //start looking for text
            } else if (token.equals("-t")) {
                user = false;
                text = true;
            }
        }
        messageList.add(new ServerMessage(everyoneInConvoButMe(ci.toString(), un.toString()), message, this));
        return messageList;
    }
    
    /**
     * called when user starts new conversation
     * @param message the START_CONVO message
     * @return a START_CONVO ServerMessage for everyone except for whoever started the convo
     */
    private ArrayList<ServerMessage> startConvo(String message) {
        ArrayList<ServerMessage> messageList = new ArrayList<ServerMessage>();
        boolean convo_id = false;
        StringBuilder ci = new StringBuilder();
        boolean user = false;
        StringBuilder un = new StringBuilder();
        for (String token: message.split(" ")) {
            //look for convo_id users that need to know about the convo
            if (convo_id && !token.equals("-u")) {
                ci.append(token);
                ci.append(" ");
            //find sender name
            } else if (user) {
                un.append(token);
            //start looking for convo id
            } if (token.equals("-s")) {
                convo_id = true;
            //convo id done, start looking for sender name
            } else if (token.equals("-u")) {
                convo_id = false;
                user = true;
            }
        }
        //add START_CONVO message for all users that were in the convo id
        messageList.add(new ServerMessage(everyoneInConvoButMe(ci.toString(), un.toString()), message, this)); 
        return messageList;
    }
    
    /**
     * called when user closes a conversation
     * @param message the CLOSE_CONVO message
     * @return a CLOSE_CONVO ServerMessage for everyone except for whoever closed the convo
     */
    private ArrayList<ServerMessage> closeConvo(String message) {
        ArrayList<ServerMessage> messageList = new ArrayList<ServerMessage>();
        boolean convo_id = false;
        StringBuilder ci = new StringBuilder();
        boolean user = false;
        StringBuilder un = new StringBuilder();
        for (String token: message.split(" ")) {
            //look for convo_id users that need to know about the convo
            if (convo_id && !token.equals("-u")) {
                ci.append(token);
                ci.append(" ");
            //look for sender's name
            } else if (user) {
                un.append(token);
            //start looking for convo_id
            } if (token.equals("-x")) {
                convo_id = true;
            //done with convo_id, start looking for sender name
            } else if (token.equals("-u")) {
                convo_id = false;
                user = true;
            }
        } 
        messageList.add(new ServerMessage(everyoneInConvoButMe(ci.toString(), un.toString()), message, this)); 
        return messageList;
    }
    
    /**
     * Make an ArrayList of sockets to send to if just returning to same client
     * @param username the username of the client to send to
     * @return ArrayList containing socket just of that username
     */
    protected ArrayList<Socket> justMe (String username) {
        ArrayList<Socket> recipients = new ArrayList<Socket>();
        recipients.add(infoMap.get(username).getSocket());
        return recipients;
    }
    
    /**
     * Make an ArrayList of sockets for everyone else but the username
     * @param username the username not to recieve the message
     * @return ArrayList with sockets for everyone else
     */
    private ArrayList<Socket> everyoneButMe (String username) {
        ArrayList<Socket> recipients = new ArrayList<Socket>();
        
        for (String un: infoMap.keySet()) {
            if (!un.equals(username)) {
                recipients.add(infoMap.get(un).getSocket());
            }
        }
        return recipients;
    }
    
    /**
     * Makes an ArrayList of sockets for everyone else in conversation but username
     * @param convoID the conversation to be updated
     * @param username the username that shouldn't be updated
     * @return ArrayList with sockets of everyone relevant
     */
    private ArrayList<Socket> everyoneInConvoButMe (String convoID, String username) {
        ArrayList<Socket> recipients = new ArrayList<Socket>();
        for (String un: convoID.split(" ")) {
            if (!un.equals(username)) {
                recipients.add(infoMap.get(un).getSocket());
            }
        }
        return recipients;
    }
    /**
     * Getter methods for some instance variables
     * @return the instance variable
     */
    public ConcurrentHashMap<String, UserInfo> getInfoMap() {
        return infoMap;
    }
    public ServerSocket getServerSocket(){
        return serverSocket; 
    }
    
    /**
     * Runs the ChatServer
     * @param args
     */
    public static void main(String[] args) {
        try {
            if(args.length == 2 && args[0].equals("-p") && Character.isDigit(args[1].charAt(1)))
            runChatServer(Integer.parseInt(args[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Makes a server and runs serve()
     * @param port the port the server should connect to
     * @throws IOException when the port is invalid
     */
    public static void runChatServer(int port) throws IOException {
        ChatServer server = new ChatServer(port);
        server.serve();
    }

}
