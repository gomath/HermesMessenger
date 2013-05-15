package client.user;

import static org.junit.Assert.*;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class ConversationTest {

    UserInfo banana = new UserInfo("Banana", "Yellow");
    UserInfo orange = new UserInfo("Banana", "Orange");
    ConcurrentHashMap<String, UserInfo> participants = new ConcurrentHashMap<String, UserInfo>();
    
    @Test
    public void addMessageTest() {
        participants.put("Banana", banana);
        participants.put("Orange", orange);
        Conversation convo = new Conversation(participants);
        Message msg = new Message(banana, convo, "Orange you glad I didn't say banana?");
        convo.addMessage(msg);
        assertEquals(convo.getMessages().get(0), msg);
    }
    
   // @Test
    

}
