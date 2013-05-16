package client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import client.gui.UserGUI;
import client.user.User;
import exceptions.InvalidUsernameException;

/**
 * Tests helper methods of ChatClientTest 
 * Strategy: uses coverage to test all the methods that can be automatically tested
 *
 */
public class ChatClientTest {

    /**
     * test that a user is set properly
     */
    @Test
    public void testSetUser() {
        User user = new User("yolo","pink",new Socket());
        ChatClient c = new ChatClient();
        c.setUser("yolo","pink",new Socket());
        
        //test for similarity
        assertEquals(user.getUsername(),c.getUser().getUsername());
        assertEquals(user.getColor(), c.getUser().getColor());
    }
    
    @Test(expected = InvalidUsernameException.class) 
    public void invalidUsernameTest() throws UnknownHostException, IOException {
        ChatClient c = new ChatClient();
        UserGUI gui = new UserGUI(c);
        c.attemptLogin("localhost", "4444", "6005isthebest", "green", gui);
    }
    

}
