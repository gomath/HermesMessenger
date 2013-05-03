package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import client.UserInfo;

public class ChatServer {
    private final ServerSocket serverSocket;
    private static ConcurrentHashMap<String, UserInfo> infoMap;
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
    public void serve() throws IOException{
        while(true) {
            //block until a client connects
            Socket socket = serverSocket.accept();
            new ChatServerThread(socket).start();
        }
    }
    
    /**
     * Handle a single client connection. Returns when client disconnects.
     * @param socket socket where the client is connected
     * @throws IOException if connection has an error or terminates unexpectedly
     */
    static void handleConnection(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = null;
        try {
            for (String line = in.readLine(); line!=null; line = in.readLine()) {
                ServerMessage outMessage = handleRequest(line, socket);
                if (outMessage != null) {
                    for (Socket recipient: outMessage.getRecipients()) {
                        out = new PrintWriter(recipient.getOutputStream(), true);
                        out.println(outMessage.getText());
                    }
                } else {
                    System.out.println("OUTPUT WAS NULL...");
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
     * @param input
     * @return
     * @throws IOException
     */
    private static ServerMessage handleRequest(String input, Socket socket) throws IOException {
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
            System.out.println("THE THING ISN'T A REAL FLAG, FOOL!");
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
    private static ServerMessage addUser(String username, String color, Socket socket) {
        if (infoMap.containsKey(username)) {
            //Username is already used, return INVALID_USER message
            return new ServerMessage(justMe(username), "-i " + username);
        } else {
            //Make new UserInfo
            infoMap.put(username, new UserInfo(username, color, socket));
            //Return ONLINE_USERS message, to be sent to everyone but me
            StringBuilder sb = new StringBuilder();
            sb.append("-f ");
            for (String un: infoMap.keySet()) {
                sb.append(un);
                sb.append(" ");
                sb.append(infoMap.get(un).getColor());
                sb.append(" ");
            } return new ServerMessage(everyoneButMe(username), sb.toString());
        }
    }
    
    /**
     * Called when client logs out
     * @param username the username of the user that logged out
     * @return OFFLINE message
     */
    private static ServerMessage logout(String username) {
        infoMap.remove(username);
        return new ServerMessage(everyoneButMe(username), "-q " + username);
    }
    
    /**
     * called when someone sends a new message
     * @param message the ADD_MSG message
     * @return UPDATE message to everyone in convo except for whoever sent the message
     */
    private static ServerMessage updateConvo(String message) {
        //Parse the message data into appropriate fields
        boolean convo_id = false;
        StringBuilder ci = new StringBuilder();
        boolean user = false;
        StringBuilder un = new StringBuilder();
        boolean text = false;
        StringBuilder msg = new StringBuilder();
        for (String token: message.split(" ")) {
            if (convo_id) {
                ci.append(token);
                ci.append(" ");
            } else if (user) {
                un.append(token);
                un.append(" ");
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
        return new ServerMessage(everyoneInConvoButMe(ci.toString(), un.toString()), message);
    }
    
    /**
     * called when user starts new conversation
     * @param message the START_CONVO message
     * @return a START_CONVO ServerMessage for everyone except for whoever started the convo
     */
    private static ServerMessage startConvo(String message) {
        boolean convo_id = false;
        StringBuilder ci = new StringBuilder();
        boolean user = false;
        StringBuilder un = new StringBuilder();
        for (String token: message.split(" ")) {
            if (convo_id) {
                ci.append(token);
                ci.append(" ");
            } else if (user) {
                un.append(token);
                un.append(" ");
            } if (token.equals("-s")) {
                convo_id = true;
            } else if (token.equals("-u")) {
                convo_id = false;
                user = true;
            }
        } return new ServerMessage(everyoneInConvoButMe(ci.toString(), un.toString()), message);   
    }
    
    /**
     * called when user closes a conversation
     * @param message the CLOSE_CONVO message
     * @return a CLOSE_CONVO ServerMessage for everyone except for whoever closed the convo
     */
    private static ServerMessage closeConvo(String message) {
        boolean convo_id = false;
        StringBuilder ci = new StringBuilder();
        boolean user = false;
        StringBuilder un = new StringBuilder();
        for (String token: message.split(" ")) {
            if (convo_id) {
                ci.append(token);
                ci.append(" ");
            } else if (user) {
                un.append(token);
                un.append(" ");
            } if (token.equals("-x")) {
                convo_id = true;
            } else if (token.equals("-u")) {
                convo_id = false;
                user = true;
            }
        } return new ServerMessage(everyoneInConvoButMe(ci.toString(), un.toString()), message);   
    }
    
    /**
     * Make an ArrayList of sockets to send to if just returning to same client
     * @param username the username of the client to send to
     * @return ArrayList containing socket just of that username
     */
    private static ArrayList<Socket> justMe (String username) {
        ArrayList<Socket> recipients = new ArrayList<Socket>();
        recipients.add(infoMap.get(username).getSocket());
        return recipients;
    }
    
    /**
     * Make an ArrayList of sockets for everyone else but the username
     * @param username the username not to recieve the message
     * @return ArrayList with sockets for everyone else
     */
    private static ArrayList<Socket> everyoneButMe (String username) {
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
    private static ArrayList<Socket> everyoneInConvoButMe (String convoID, String username) {
        ArrayList<Socket> recipients = new ArrayList<Socket>();
        for (String un: convoID.split(" ")) {
            if (!un.equals(username)) {
                recipients.add(infoMap.get(un).getSocket());
            }
        }
        return recipients;
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void runChatServer(int port) throws IOException {
        ChatServer server = new ChatServer(port);
        server.serve();
    }

}
