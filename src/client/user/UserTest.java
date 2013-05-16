package client.user;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class UserTest {
    //Tests that a user is created properly
    @Test
    public void usernameColorSocketTest() {
        String username1 = "Banana";
        String color1 = "yellow";
        Socket socket1 = new Socket();
        User user = new User(username1, color1, socket1);
        assertEquals(user.getUsername(), username1);
        assertEquals(user.getColor(), color1);
        assertEquals(user.getSocket(), socket1);   
    }
    
    /**
     * HANDLE CONNECTION
     * Can't be tested with unit tests because of required socket connection.
     * Has been visually verified to work because the connection between Client
     * and Server is working, and all of the incoming messages from the server
     * are being processed and making the appropriate changes in the GUI.
     */
    
    /**
     * HANDLE REQUEST
     * Also can't be unit tested because of the required connection to a server.
     * Visually tested by manually stepping through actions to induce all possible
     * messages from the server (listed in the Client/Server protocol documentation).
     * These incoming messages were successfully received (verified through print statements),
     * and were processed correctly, making the desired changes in the GUI.
     */
    
    /**
     * SEND MESSAGE TO SERVER
     * Can't be unit tested without a server. Visually tested by printing incoming
     * messages on the server. Actions on the client are properly composed into outgoing
     * messages and are successfully received by the server.
     */
    
    /**
     * START CONVO
     * Can't be unit tested without a server. Visually tested by successfully initializing
     * conversations, and printing MyConvos, which keeps track of all of the users active
     * conversations.
     */
    
    /**
     * CLOSE CONVO
     * Can't be unit tested without a server. Visually tested by successfully closing
     * conversations, and printing MyConvos, which keeps track of all of the users active
     * conversations.
     */
    
    /**
     * ADD MSG TO CONVO
     * Can't be tested without a server. Visually tested by sending a message to another user.
     * Visually tested by GUI updating with new message, and the correct message being received
     * by the server.
     */
    
    /**
     * UPDATE CONVO
     * Can't be tested without a server. Visually tested. The incoming message from the server
     * is correctly parsed, added to the Conversation, and displayed in the GUI.
     */
    
    /**
     * LOGIN
     * Can't be tested without a server. Testing using print statements, the login message is
     * properly created, sent, and received by the server.
     */
    
    /**
     * QUIT
     * Can't be tested without a server. Visually tested. The login view is opened, all the user's
     * conversations are closed, and the quit message is correctly generated, sent, and received
     * by the server.
     */
    
    //Tests setting Online Users
    @Test
    public void setOnlineUsersTest() {
        String username1 = "Banana";
        String color1 = "yellow";
        Socket socket1 = new Socket();
        User user = new User(username1, color1, socket1);
        ConcurrentHashMap<String, UserInfo> userMap = new ConcurrentHashMap<String, UserInfo>();
        UserInfo ui = new UserInfo("Blobberty", "orange");
        userMap.put("Daniel", ui);
        userMap.put("Marianne", ui);
        userMap.put("Blobberty", ui);
        user.setOnlineUsers(userMap);
        assertEquals(user.getOnlineUsers().keySet(), userMap.keySet());
    }
    
    /**
     * ADD NEW MY CONVO
     * Can't be tested without GUI. Tested visually. Conversations are properly created and added
     * to MyConvos. The GUI is properly updated with the new tab, and the content is filled in the tab.
     */
    
    /**
     * REMOVE MY CONVO
     * Can't be tested without GUI. Tested visually. Conversations are properly removed from MyConvos,
     * and added to the inactiveConvos. The GUI responds correctly, removing the conversation tab, and
     * saving the history in inactiveConvos so it can be viewed later.
     */
    
    /**
     * CHECK DUPLICATE CONVO
     * Can't be tested without GUI. Tested visually. Attempting to create a convo that already exists,
     * and the appropriate exception in thrown.
     */
}