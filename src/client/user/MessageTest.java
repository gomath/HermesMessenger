package client.user;

import static org.junit.Assert.*;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

/**
 * Test each getter method (and therefore the constructor) of the Message class.
 *
 */
public class MessageTest {
    /**
     * Test that the getSender() method returns accurately
     */
    @Test
    public void messageSender() {
        UserInfo banana = new UserInfo("Banana", "Yellow");
        UserInfo orange = new UserInfo("Banana", "Orange");
        ConcurrentHashMap<String, UserInfo> participants = new ConcurrentHashMap<String, UserInfo>();
        participants.put("Banana", banana);
        participants.put("Orange", orange);
        Conversation convo = new Conversation(participants);
        Message msg = new Message(banana, convo, "Orange you glad I didn't say banana?");
        assertEquals(msg.getSender(), banana);
    }

    /**
     * Test that the getConvo() method returns accurately
     */
    @Test
    public void messageConvo() {
        UserInfo banana = new UserInfo("Banana", "Yellow");
        UserInfo orange = new UserInfo("Banana", "Orange");
        ConcurrentHashMap<String, UserInfo> participants = new ConcurrentHashMap<String, UserInfo>();
        participants.put("Banana", banana);
        participants.put("Orange", orange);
        Conversation convo = new Conversation(participants);
        Message msg = new Message(banana, convo, "Orange you glad I didn't say banana?");
        assertEquals(msg.getConvo(), convo);
    }

    /**
     * Test that the getText() method returns accurately
     */
    @Test
    public void messageText() {
        UserInfo banana = new UserInfo("Banana", "Yellow");
        UserInfo orange = new UserInfo("Banana", "Orange");
        ConcurrentHashMap<String, UserInfo> participants = new ConcurrentHashMap<String, UserInfo>();
        participants.put("Banana", banana);
        participants.put("Orange", orange);
        Conversation convo = new Conversation(participants);
        Message msg = new Message(banana, convo, "Orange you glad I didn't say banana?");
        assertEquals(msg.getText(), "Orange you glad I didn't say banana?");
    }

}
