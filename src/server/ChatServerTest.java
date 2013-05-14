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
 * 
 * 
 *
 */
public class ChatServerTest {

    @Test
    /**
     * Tests that login message from client is handled appropriately
     */
    public void loginTest() throws IOException, InterruptedException {
        Socket socket = new Socket();
        ArrayList<ServerMessage> message = ChatServer.handleClientRequest("-l guillermo black",socket);
        for(ServerMessage msg: message) {
            System.out.println(msg.getText());
        }
        String msg1 = "-f \n";
        String msg2 = "-o guillermo black\n";
        assertEquals(msg1, message.get(0).getText());
        assertEquals(msg2, message.get(1).getText());
        socket.close();
    }
    
}
