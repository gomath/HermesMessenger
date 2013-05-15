package server;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import client.User;
/**
 * Partitions the input space to test handleClientRequest
 * 
 *
 */
public class ChatServerTest {

    @Test
    /**
     * Tests handleClientRequest for login
     */
    public void loginTest() throws IOException, InterruptedException {
        Socket socket = new Socket();
        ArrayList<ServerMessage> message = ChatServer.handleClientRequest("-l guillermo black",socket);
        
        //make sure appropriate things contained
        String msg1 = "-f \n";
        String msg2 = "-o guillermo black\n";
        assertEquals(msg1, message.get(0).getText());
        assertEquals(msg2, message.get(1).getText());
        socket.close();
    }
    
    @Test
    /**
     * Test logging out
     */
    public void quitTest() throws IOException, InterruptedException {
        Socket socket = new Socket();
        ArrayList<ServerMessage> message = ChatServer.handleClientRequest("-q username", socket);
        
        String msg = "-q username\n";
        assertEquals(msg, message.get(0).getText());
        
        socket.close();
    }
    
    
    //@Test
    /**
     * Test 
     */
    public void sendMessageTest() throws IOException, InterruptedException {
        Socket socket = new Socket();
        ArrayList<ServerMessage> message = 
                ChatServer.handleClientRequest("-c banana orange -u banana -t orange you glad I didn't say banana?", socket);
        
        for(ServerMessage msg: message) {
            System.out.println(msg.getText());
        }
    }
    
}
