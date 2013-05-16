package server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.Test;

/**
 * @category no_didit
 *
 */
public class ChatServerNoDiditTest {
    
    /**
     * Runs the server necessary to test server methods start convo and send message
     */
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
     * Makes sure no exceptions are thrown
     * @throws UnknownHostException
     * @throws IOException
     */
    public void loginTest() throws UnknownHostException, IOException, InterruptedException {
        
        String connectInfo = "-l first black\n";
        Socket socket = new Socket("localhost", 4444);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.print(connectInfo);
        out.flush();

        for (String line =in.readLine(); line!=null; line=in.readLine()) {
            if (line.length() != 0) {
                String loginmsg = line;
                assertEquals("-f ", loginmsg);
                return;
                
            }
        }
        //String logout = "-q gomath\n";
        //out.print(logout);
        //out.flush();
        //socket.close(); 
       
        
    } 
    
    @Test
    /**
     * Make sure if a user logs out the other online user(s) gets a message about it
     * @throws UnknownHostException
     * @throws IOException
     */
    public void logoutTest() throws UnknownHostException, IOException, InterruptedException {
        String connectInfo = "-l dale red\n";
        String logoutInfo = "-q dale";
        Socket socket = new Socket("localhost", 4444);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.print(connectInfo);
        out.flush();
        
        String connectInfo2 = "-l jim red\n";
        String logoutInfo2 = "-q jim";
        Socket socket2 = new Socket("localhost", 4444);
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        
        out2.print(connectInfo2);
        out2.flush();
        
        in2.readLine();
        in2.readLine();
  
        out.println(logoutInfo);
        out.flush();

        String line1 = in.readLine();
        String line2 = in.readLine();
        
        System.out.println("line1: "+line1);
        System.out.println("line2: "+line2);
        
        //String logout = "-q gomath\n";
        out.println(logoutInfo);
        out.flush();
        
        String logoutConfirmed = in2.readLine();
        
        out2.println("-q jim");
        out2.flush();
        
        assertEquals("-q dale", logoutConfirmed);

       
        
    } 
    
    @Test
    /**
     * Make sure if a user logs out the other online user(s) gets a message about it
     * @throws UnknownHostException
     * @throws IOException
     */
    public void startConvoTest() throws UnknownHostException, IOException, InterruptedException {
        String connectInfo = "-l loras yellow\n";
        String logoutInfo = "-q loras";
        Socket socket = new Socket("localhost", 4444);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        out.print(connectInfo);
        out.flush();
        
        String connectInfo2 = "-l renly red\n";
        String logoutInfo2 = "-q renly";
        String messageInvite = "-s loras renly -u renly \n";
        Socket socket2 = new Socket("localhost", 4444);
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        
        
        out2.print(connectInfo2);
        out2.flush();
        out2.print(messageInvite);
        out2.flush();
        
        in2.readLine();
        in2.readLine();
        
        //in.readLine();
        System.out.println("line "+in.readLine());
  
        out.println(logoutInfo);
        out.flush();
        
        System.out.println(in2.readLine());
        out2.println(logoutInfo2);
        out2.flush();
        
        //assertEquals("-q loras", logoutConfirmed);

       
        
    } 
    
   @Test
    /**
     * test multiple interactions - logging in, starting convo, sending msg, logging out
     */
    public void compositeTest() throws UnknownHostException, IOException {
        Socket socket1 = new Socket("localhost", 4444);
        Socket socket2 = new Socket("localhost", 4444);
        Socket socket3 = new Socket("localhost", 4444);
        ArrayList<ServerMessage> gomathMsgs = ChatServer.handleClientRequest("-l gomath black",socket1);
        
 
        assertEquals("-f first black renly red \n", gomathMsgs.get(0).getText());
        assertEquals("-o gomath black\n", gomathMsgs.get(1).getText());
        ArrayList<ServerMessage> jenMsgs = ChatServer.handleClientRequest("-l jtilton purple",socket2);
        System.out.println("jtilton messages: \n");
        
        assertEquals("-f first black renly red gomath black \n", jenMsgs.get(0).getText());
        assertEquals("-o jtilton purple\n", jenMsgs.get(1).getText());
        ArrayList<ServerMessage> danMsgs = ChatServer.handleClientRequest("-l disanto blue", socket3);
        System.out.println("disanto attempt messages:\n");
        
        assertEquals("-f first black renly red jtilton purple gomath black \n", danMsgs.get(0).getText());
        assertEquals("-o disanto blue\n", danMsgs.get(1).getText());
        
    }
    

    


}
