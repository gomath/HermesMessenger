package client.user;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class ConversationTest {

    UserInfo banana = new UserInfo("Banana", "Yellow");
    UserInfo orange = new UserInfo("Orange", "Orange");
    ConcurrentHashMap<String, UserInfo> participants = new ConcurrentHashMap<String, UserInfo>();
    /**
     * Test the 3 getter methods: getMessages(), getConvoID(), and getParticipantsMap()
     */
    @Test
    public void getMessageTest() {
        participants.put("Banana", banana);
        participants.put("Orange", orange);

        Conversation convo = new Conversation(participants);
        Message msg1 = new Message(banana, convo, "Orange you glad I didn't say banana?");
        Message msg2 = new Message(orange, convo, "You're annoying");
        convo.addMessage(msg1);
        convo.addMessage(msg2);
        List<Message> msgList = convo.getMessages();
        List<Message> answer = new ArrayList<Message>();
        answer.add(msg1);
        answer.add(msg2);
        assertEquals(msgList, answer);
    }
    @Test
    public void getConvoID() {
        participants.put("Banana", banana);
        participants.put("Orange", orange);

        Conversation convo = new Conversation(participants);
        String convoID = convo.getConvoID();
        String answer = "Banana Orange ";
        assertEquals(convoID, answer);
    }
    @Test
    public void getPartsMap() {
        participants.put("Banana", banana);
        participants.put("Orange", orange);

        Conversation convo = new Conversation(participants);
        ConcurrentHashMap<String, UserInfo> map = convo.getParticipantsMap();
        ConcurrentHashMap<String, UserInfo> answer = new ConcurrentHashMap<String, UserInfo>();
        answer.put("Banana", banana);
        answer.put("Orange", orange);
        assertEquals(map, answer);
    }
    
    //Make sure addMessage works correctly
    @Test
    public void addMessageTest() {
        participants.put("Banana", banana);
        participants.put("Orange", orange);
        Conversation convo = new Conversation(participants);
        Message msg = new Message(banana, convo, "Orange you glad I didn't say banana?");
        convo.addMessage(msg);
        assertEquals(convo.getMessages().get(0), msg);
    }
    //Test the alphabetizing method on both empty and nonempty hash maps
    //(partitioning the input space)
    @Test
    public void alphabetizeTest() {
        participants.put("Banana", banana);
        participants.put("Orange", orange);
        participants.put("Apple", new UserInfo("Apple", "Red"));
        Conversation convo = new Conversation(participants);
        assertEquals(convo.alphabetizeHashMap(participants), "Apple Banana Orange ");
    }    
    @Test
    public void emptyAlphabetizeTest() {
        Conversation convo = new Conversation(participants);
        assertEquals(convo.alphabetizeHashMap(participants), "");
    } 
}
