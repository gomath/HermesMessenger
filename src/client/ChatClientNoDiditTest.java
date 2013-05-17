package client;



import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import server.ChatServer;

import client.gui.UserGUI;
import exceptions.InvalidUsernameException;

/**
 * @category no_didit
 * OVERALL TESTING STRATAGEM 
 * Testing the only other method that can be reasonably testing with automatic tests
 * by partitioning the input space - trying both a valid and invalid username.
 * The second test is an integration test and uses a ChatServer to accept the connection
 */
public class ChatClientNoDiditTest {

    @Test(expected = InvalidUsernameException.class) 
    public void invalidUsernameTest() throws UnknownHostException, IOException {
        ChatClient c = new ChatClient();
        UserGUI gui = new UserGUI(c);
        c.attemptLogin("localhost", "4444", "6005isthebest", "green", gui);
    }
    
    @Test
    /**
     * test valid login info with integration test involving ChatServer and ChatClient
     */
    public void singleUserIntegratedLoginTest() throws IOException {
        ChatServer s = new ChatServer(4444);
        ChatClient c = new ChatClient();
        UserGUI gui = new UserGUI(c);
        c.attemptLogin("localhost", "4444", "gomath", "green", gui);
        assertEquals("gomath", c.getUser().getUsername());
        
        //didn't want to leave an unused variable lying around 
        assertEquals("class server.ChatServer",s.getClass().toString());
    }
    

}
