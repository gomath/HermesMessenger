package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private final ServerSocket serverSocket;
    private ConcurrentHashMap<String,Socket> nameSock;
    
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
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        try {
            for (String line = in.readLine(); line!=null; line = in.readLine()) {
                String output = handleRequest(line);
                if(output != null) {
                    out.println(output);
                }
                else {
                    System.out.println("What is even going on??????????????????");
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
    private static String handleRequest(String input) throws IOException {
        String [] tokens = input.split(" ");
        String flag = tokens[0];
        
        if(flag.equals("-l")) {
            System.out.println("login");
        } else if(flag.equals("-q")) {
            System.out.println("quit");
        } else if(flag.equals("-c")) {
            System.out.println("add_message");
        } else if(flag.equals("-s")) {
            System.out.println("start_convo");
        } else if(flag.equals("-x")) {
            System.out.println("close_convo");
        } else {
            System.out.println("ohnoyoudidn't");
        }
        
        return null;
    }

    /**
     * Runs the ChatServer
     * @param args
     */
    public static void main(String[] args) {
        try {
            runChatServer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }
    
    public static void runChatServer() throws IOException {
        final int port = 4444;
        ChatServer server = new ChatServer(port);
        server.serve();
    }

}
