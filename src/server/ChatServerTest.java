package server;

import static org.junit.Assert.*;


import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import client.user.UserInfo;

/**
 * OVERALL TESTING STRATEGEM
 * Partitions the input space to test inputs to handleClientRequest that can be handled without creating real sockets
 * More extensive testing of message passing in ChatServerNoDiditTest.java
 * ALSO: tested all testable method to ensure full coverage
 *
 */
public class ChatServerTest {
    
    @Test
    /**
     * tests adding a user - makes sure correct msgs returned
     * 
     */
    public void addUserTest() throws IOException {
        Socket socket = new Socket();
        ChatServer s = new ChatServer(4443);
        ArrayList<ServerMessage> msg = s.addUser("gomath","black", socket);
        assertEquals("-o gomath black\n", msg.get(1).getText());
        assertEquals("-f \n", msg.get(0).getText());
    }
    
    @Test
    /**
     * make fake userInfo map and test setting infoMap
     */
    public void getInfoMapTest() throws IOException {
        UserInfo don = new UserInfo("dondraper", "black", new Socket());
        ConcurrentHashMap<String,UserInfo> mappy = new ConcurrentHashMap<String,UserInfo>();
        mappy.put("don", don);
        ChatServer s = new ChatServer(5000);
        s.infoMap = mappy;
        assertEquals(mappy, s.getInfoMap());
        
    }
    
    @Test
    /**
     * make fake userInfo map and test setting infoMap
     */
    public void justMeTest() throws IOException {
        Socket socket = new Socket();
        UserInfo don = new UserInfo("dondraper", "black", socket);
        ConcurrentHashMap<String,UserInfo> mappy = new ConcurrentHashMap<String,UserInfo>();
        mappy.put("don", don);
        ChatServer s = new ChatServer(5001);
        s.infoMap = mappy;
        assertEquals(socket,s.justMe("don").get(0));
        
    }

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
