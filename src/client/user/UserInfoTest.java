package client.user;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.Test;

/**
 * TESTING STRATEGY
 * The UserInfo class is pretty simple, it's just for storing
 * information about the users. The unit tests will ensure data
 * is properly getting stored as attributes and retrieving the
 * right information.
 *
 */
public class UserInfoTest {
    //TEST GET USERNAME
    @Test
    public void usernameTest() {
        UserInfo orange = new UserInfo("Orange", "orange", new Socket());
        UserInfo banana = new UserInfo("Banana", "yellow");
        assertEquals("Orange", orange.getUsername());
        assertEquals("Banana", banana.getUsername());
    }
    //TEST GET COLOR
    @Test
    public void colorTest() {
        UserInfo orange = new UserInfo("Orange", "orange", new Socket());
        UserInfo banana = new UserInfo("Banana", "yellow");
        assertEquals("orange", orange.getColor());
        assertEquals("yellow", banana.getColor());
    }
    //TEST GET SOCKET
    @Test
    public void socketTest() {
        Socket socky = new Socket();
        UserInfo orange = new UserInfo("Orange", "orange", socky);
        assertEquals(socky, orange.getSocket());
    }
}
