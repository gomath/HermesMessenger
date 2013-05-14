package server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.Test;

public class ChatServerNoDiditTest {

    @Test
    /**
     * Tests that login message from client is handled appropriately
     */
    public void loginTest() throws IOException, InterruptedException {
        Socket socket = new Socket();
        ArrayList<ServerMessage> message = ChatServer.handleClientRequest("-l guillermo black",socket);
        for(ServerMessage msg: message) {
            System.out.println(msg.getText());
        }
        socket.close();
    }
    
   //@Test
    /**
     * test multiple users?
     */
    public void manyUsers() throws UnknownHostException, IOException {
        Socket socket1 = new Socket("localhost", 4444);
        Socket socket2 = new Socket("localhost", 4444);
        Socket socket3 = new Socket("localhost", 4444);
        ArrayList<ServerMessage> message1 = ChatServer.handleClientRequest("-l gomath black",socket1);
        System.out.println("gomath attempt messages:\n");
        for(ServerMessage message: message1) {
            System.out.println(message.getText());
        }
        ArrayList<ServerMessage> message2 = ChatServer.handleClientRequest("-l jtilton purple",socket2);
        System.out.println("jtilton messages: \n");
        for(ServerMessage message: message2) {
            System.out.println(message.getText());
        }
        ArrayList<ServerMessage> message3 = ChatServer.handleClientRequest("-l disanto blue", socket3);
        System.out.println("disanto attempt messages:\n");
        for(ServerMessage message: message3) {
            System.out.println(message.getText());
        }
        
    }
    
    //@Test
    /**
     * test socket/server communication
     * @throws IOException
     * @throws InterruptedException
     */
    public void commTest() throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 4444);
        System.out.println("connection success");
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
        out.print("-l mathgos black");
    
        for (String line = in.readLine(); line!=null; line = in.readLine()) {
                
                System.out.println(line);
        
        }
        socket.close();
    }
    
    @Test
    /**
     * Makes sure no exceptions are thrown
     * @throws UnknownHostException
     * @throws IOException
     */
    public void runServerTest() throws UnknownHostException, IOException, InterruptedException {
        class serverThread implements Runnable {

            @Override
            public void run() {
                ChatServer.main(new String[] {"-p","4444"});
                
            } 
        }
        serverThread myRunnableThread = new serverThread();
        Thread myThread = new Thread(myRunnableThread);
        myThread.start();
        String connectInfo = "-l gomath black\n";
        Socket socket = new Socket("localhost", 4444);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.print(connectInfo);
        out.flush();

        for (String line =in.readLine(); line!=null; line=in.readLine()) {
            if (line.length() != 0) {
                System.out.println("CLIENT INBOX: " + line);
            }
        }
        String logout = "-q gomath\n";
        out.print(logout);
        out.flush();
        socket.close(); 
       
        
    } 

}
