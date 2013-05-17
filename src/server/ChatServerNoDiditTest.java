package server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

/**
 * @category no_didit
 * OVERALL TESTING STRATAGEM:
 * This is an integration test that tests the methods of the server that cannot be tested without a real socket. 
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
     * test user logging in that appropriate messages are sent
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
                //message we want - message sent from server when user logs in
                String loginmsg = line;
                
                //let's log the user out so as to not interfere with subsequent tests
                String logout = "-q gomath\n";
                out.print(logout);
                out.flush();
                
                assertEquals("-f ", loginmsg);
                return;
                
            }
        }

       
        
    } 
    
    @Test
    /**
     * Make sure if a user logs out the other online user(s) gets a message about it
     * @throws UnknownHostException
     * @throws IOException
     */
    public void logoutTest() throws UnknownHostException, IOException, InterruptedException {
        //info for our first user
        String connectInfo = "-l dale red\n";
        String logoutInfo = "-q dale";
        Socket socket = new Socket("localhost", 4444);
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        //connect our first user
        out.print(connectInfo);
        out.flush();
        
        //info for our second user
        String connectInfo2 = "-l jim red\n";
        Socket socket2 = new Socket("localhost", 4444);
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        
        //login our second user
        out2.print(connectInfo2);
        out2.flush();
        
        //read socket input stream so when user 1 logs out the logout message will be next in line to read
        in2.readLine();
        in2.readLine();
  
        out.println(logoutInfo);
        out.flush();        
        
        //String logout = "-q gomath\n";
        out.println(logoutInfo);
        out.flush();
        
        //this is the message we want to snatch (means user 2 knows of user 1's logging out - logout msg success!")
        String logoutConfirmed = in2.readLine();
        
        out2.println("-q jim");
        out2.flush();
        
        assertEquals("-q dale", logoutConfirmed);

       
        
    } 
    
    @Test
    /**
     * Make sure start convo causes right messages to be sent
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
        
        //log the second user in
        out2.print(connectInfo2);
        out2.flush();
        
        //invite loras to a convo
        out2.print(messageInvite);
        out2.flush();
        
        //get to the start convo msg
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        
        //snatch start convo msg
        String startConvoMsg = in.readLine();
        
       
        //log the users out so doesn't interfere with subsequent tests
        out.println(logoutInfo);
        out.flush();
        
        out2.println(logoutInfo2);
        out2.flush();
        
        //checks that the invited party was notified of a convo starting
        assertEquals("-s loras renly -u renly ", startConvoMsg);

       
    } 
    
    @Test
    /**
     * Make sure if a user sends a message for a convo, the other user in the convo gets the appropriate msg from the 
     * server to indicate this
     * @throws UnknownHostException
     * @throws IOException
     */
    public void sendMsgTest() throws UnknownHostException, IOException, InterruptedException {
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
        String messageToSend = "-c loras renly -u renly -t yo whattup homedawg";
        Socket socket2 = new Socket("localhost", 4444);
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
        
        //log the second user in
        out2.print(connectInfo2);
        out2.flush();
        
        //invite loras to a convo
        out2.print(messageInvite);
        out2.flush();
        
        //send loras a msg
        out2.println(messageToSend);
        out2.flush();
        
        //get to the start convo msg
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        
        String msgFromRenly = in.readLine();
        
        
        //snatch start convo msg
        //String startConvoMsg = in.readLine();
        
       
        //log the users out so doesn't interfere with subsequent tests
        out.println(logoutInfo);
        out.flush();
        
        out2.println(logoutInfo2);
        out2.flush();
        
        //checks that the invited party was notified of a convo starting
        assertEquals("-c loras renly -u renly -t yo whattup homedawg", msgFromRenly);

    } 
    
    @Test
    /**
     * Make sure if a user leaves a convo, the other user is appropriately notified
     * @throws UnknownHostException
     * @throws IOException
     */
    public void exitConvoTest() throws UnknownHostException, IOException, InterruptedException {
        //first user's info
        String connectInfo = "-l loras yellow\n";
        String logoutInfo = "-q loras";
        Socket socket = new Socket("localhost", 4444);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //log the first user in
        out.print(connectInfo);
        out.flush();
        
        //second user's info
        String connectInfo2 = "-l renly red\n";
        String logoutInfo2 = "-q renly";
        String messageInvite = "-s loras renly -u renly \n";
        String leaveConvo = "-x loras renly -u renly ";
        Socket socket2 = new Socket("localhost", 4444);
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
        
        //log the second user in
        out2.print(connectInfo2);
        out2.flush();
        
        //invite loras to a convo
        out2.print(messageInvite);
        out2.flush();
        
        //have renly leave the convo
        out2.println(leaveConvo);
        out2.flush();
        
        //get to the start convo msg
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        in.readLine();
        
        //message that we want - the message that renly left/closed the convo
        String msgFromRenly = in.readLine();
        
        
        //snatch start convo msg
        //String startConvoMsg = in.readLine();
        
       
        //log the users out so doesn't interfere with subsequent tests
        out.println(logoutInfo);
        out.flush();
        
        out2.println(logoutInfo2);
        out2.flush();
        
        //checks that the other party was notified of a party leaving/closing the convo
        assertEquals("-x loras renly -u renly ", msgFromRenly);

    } 
    
   @Test
    /**
     * test group chat - make sure everyone else got invite, make sure everyone but user got sent msg
     */
    public void groupChatTest() throws UnknownHostException, IOException {
        Socket socket1 = new Socket("localhost", 4444);
        Socket socket2 = new Socket("localhost", 4444);
        Socket socket3 = new Socket("localhost", 4444);
        
        PrintWriter out1 = new PrintWriter(socket1.getOutputStream());
        BufferedReader in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        String login1 = "-l marianne black";
        out1.println(login1);
        out1.flush();
        
        
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
        BufferedReader in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        String login2 = "-l jenn purple";
        out2.println(login2);
        out2.flush();
        
        PrintWriter out3 = new PrintWriter(socket3.getOutputStream());
        BufferedReader in3 = new BufferedReader(new InputStreamReader(socket3.getInputStream()));
        String login3 = "-l dan blue";
        out3.println(login3);
        out3.flush();
        
        //jenn initiates group chat
        out2.println("-s dan jenn marianne -u jenn");
        out2.flush();
        
        //dan says something
        out3.println("-c dan jenn marianne -u dan -t hallo");
        out3.flush();
        
        //clear marianne's msgs we don't care about
        in1.readLine();
        in1.readLine();
        in1.readLine();
        in1.readLine();
        in1.readLine();
        in1.readLine();
       
        String marianneInvite = in1.readLine();
        in1.readLine();
        String marrianeReceivedMsg = in1.readLine();
        
        //clear jenn's msgs we don't care about
        in2.readLine();
        in2.readLine();
        in2.readLine();
        in2.readLine();
        String jennReceivedMsg = in2.readLine();
        
        //clear dan's msgs we don't care about
        in3.readLine();
        in3.readLine();
        
        //snatch dan's invite to the group chat
        String danInvite = in3.readLine();
        
        //make sure everyone got the right invite (initiator does not receive a msg)
        assertEquals("-s dan jenn marianne -u jenn", marianneInvite);
        assertEquals("-s dan jenn marianne -u jenn", danInvite);
        
        //make sure people get sent msg (sender does not get sent msg)
        assertEquals("-c dan jenn marianne -u dan -t hallo", marrianeReceivedMsg);
        assertEquals("-c dan jenn marianne -u dan -t hallo", jennReceivedMsg);
        
        
    }
    

    


}
