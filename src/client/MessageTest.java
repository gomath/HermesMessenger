package client;

import static org.junit.Assert.*;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class MessageTest {

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
