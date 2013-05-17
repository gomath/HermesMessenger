package client.gui;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import client.user.Conversation;
import client.user.User;
import client.user.UserInfo;
/**
 * @category no_didit
 * Our visual tests for TabPanel ensure that the panel is correctly generated
 * and that the methods that fill in the conversation history do so
 * accurately. Multiple open tabs, multiple user conversations, and repeatedly
 * opening and closing conversations were all tested.
 *
 */
public class TabPanelNoDiditTest {
    //Test creation of a tab panel
    @Test
    public void tabPanelTest() {
        User user = new User("Banana", "yellow", new Socket());
        UserInfo banana = new UserInfo("Banana", "Yellow");
        UserInfo orange = new UserInfo("Orange", "Orange");
        ConcurrentHashMap<String, UserInfo> participants = new ConcurrentHashMap<String, UserInfo>();
        participants.put("Banana", banana);
        participants.put("Orange", orange);
        Conversation convo = new Conversation(participants);
        TabPanel tp = new TabPanel(convo, user);
        assertEquals(convo,  tp.getConvo());
    }
    
    /**
     * MAKE PANEL
     * Tested visually. The panel contains the correct components
     * that are described in the user experience section of our design
     * documentation. The panel appears formatted correctly and the
     * components all react with the appropriate actions when interacted
     * with by the user.
     */
    
    /**
     * SHOW MESSAGE
     * Tested visually. This method is called each time a new message
     * is sent or received and must display in the conversation history
     * panel. We ensured that the message is always correctly added including
     * when multiple tabs are open.
     */
    
    /**
     * FILL HISTORY
     * Tested visually. Fill history is called when a conversation is re-opened
     * and the past chat history must be displayed. The visual testing involved
     * closing and reopening conversations between two users and more users, and
     * verifying that the past chat history was accurately reproduced.
     */

}
