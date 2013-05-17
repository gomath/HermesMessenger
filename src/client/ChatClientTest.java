package client;

import static org.junit.Assert.*;


import java.net.Socket;

import org.junit.Test;

import client.user.User;

/**
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
     * public void attemptLogin(String IP, String port, String username,String color, UserGUI, gui)
     * tested with valid inputs manually when the system is manually tested and visually inspected
     * tested that responds to invalid input appropriately in ChatClientNoDiditTest
     */
    
    /**
     * public User getUser()
     * tested in conjunction with setUser
     */
    
    /**
     * public void runUser()
     * tested manually when Client is run - can tell if we are able to do things in the client
     * like login and start conversations and receive messages from other users since User handles 
     * the client server interaction
     */
    
    /**
     * public static void main(String[] args)
     * tested in the visual system test and can verify it works because
     * everything 
     */

    

}
