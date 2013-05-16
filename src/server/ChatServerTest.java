package server;

import static org.junit.Assert.*;


import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.junit.Test;

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
        ChatServer s = new ChatServer(4444);
        ArrayList<ServerMessage> message = s.handleClientRequest("-l guillermo black",socket);
        
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
        ChatServer s = new ChatServer(4445);
        ArrayList<ServerMessage> message = s.handleClientRequest("-q username", socket);
        
        String msg = "-q username\n";
        assertEquals(msg, message.get(0).getText());
        
        socket.close();
    }
    
    
    
}
