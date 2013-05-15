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

/**
 * @category no_didit
 *
 */
public class ChatServerNoDiditTest {
    
    @Test
    public void runAServer() {
        class serverThread implements Runnable {

            @Override
            public void run() {
                ChatServer.main(new String[] {"-p","4444"});
                
            } 
        }
        serverThread myRunnableThread = new serverThread();
        Thread myThread = new Thread(myRunnableThread);
        myThread.start();
    }

   @Test
    /**
     * test multiple users?
     */
    public void manyUsers() throws UnknownHostException, IOException {
        Socket socket1 = new Socket("localhost", 4444);
        Socket socket2 = new Socket("localhost", 4444);
        Socket socket3 = new Socket("localhost", 4444);
        ArrayList<ServerMessage> gomathMsgs = ChatServer.handleClientRequest("-l gomath black",socket1);
        
 
        assertEquals("-f \n", gomathMsgs.get(0).getText());
        assertEquals("-o gomath black\n", gomathMsgs.get(1).getText());
        ArrayList<ServerMessage> jenMsgs = ChatServer.handleClientRequest("-l jtilton purple",socket2);
        System.out.println("jtilton messages: \n");
        
        assertEquals("-f gomath black \n", jenMsgs.get(0).getText());
        assertEquals("-o jtilton purple\n", jenMsgs.get(1).getText());
        ArrayList<ServerMessage> danMsgs = ChatServer.handleClientRequest("-l disanto blue", socket3);
        System.out.println("disanto attempt messages:\n");
        
        assertEquals("-f jtilton purple gomath black \n", danMsgs.get(0).getText());
        assertEquals("-o disanto blue\n", danMsgs.get(1).getText());
        
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
    
    //@Test
    /**
     * Makes sure no exceptions are thrown
     * @throws UnknownHostException
     * @throws IOException
     */
    public void runServerTest() throws UnknownHostException, IOException, InterruptedException {
        
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
