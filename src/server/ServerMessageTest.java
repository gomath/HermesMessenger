package server;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;

import org.junit.Test;

public class ServerMessageTest {
    //Test fetching recipients
    @Test
    public void getRecipientsTest() {
        ArrayList<Socket> recipients = new ArrayList<Socket>();
        recipients.add(new Socket());
        recipients.add(new Socket());
        recipients.add(new Socket());
        ServerMessage sm = new ServerMessage(recipients, "I love 6.005!");
        assertEquals(recipients, sm.getRecipients());
    }
    
    //Test fetching Text
    @Test
    public void getTextTest() {
        ArrayList<Socket> recipients = new ArrayList<Socket>();
        recipients.add(new Socket());
        recipients.add(new Socket());
        recipients.add(new Socket());
        ServerMessage sm = new ServerMessage(recipients, "I love 6.005!");
        assertEquals("I love 6.005!\n", sm.getText());
    }
    
    /**
     * TO STRING
     * Can't be unit tested without server. Visually verified to work,
     * method is used only for debug purposes anyway.
     */
}
