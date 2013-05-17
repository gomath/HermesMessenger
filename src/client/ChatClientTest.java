package client;

import static org.junit.Assert.*;


import java.net.Socket;

import org.junit.Test;

import client.user.User;

/**
 * OVERALL TESTING STRATEGEM
 * Tests helper methods of ChatClientTest that can be tested automatically
 * Strategy: uses coverage to test all the methods that can be automatically tested
 *
 */
public class ChatClientTest {
    /**
     * public ChatClient()
     * This is tested manually when the client is run, the main method of
     * ChatClient is called, which calls the constructor of ChatClient
     * 
     * ChatClient runs the gui on the EDT
     */
     
    
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
    
    /**
     * ATTEMPTLOGIN
     * tested with valid inputs manually when the system is manually tested and visually inspected
     * tested that responds to invalid input appropriately in ChatClientNoDiditTest
     */
    
    /**
     * GETUSER
     * tested in conjunction with setUser
     */
    
    /**
     * RUNUSER
     * tested manually when Client is run - can tell if we are able to do things in the client
     * like login and start conversations and receive messages from other users since User handles 
     * the client server interaction
     */
    
    /**
     * MAIN
     * tested in the visual system test and can verify it works because
     * everything shows up as expected and the client behaves as expected
     */

    

}
