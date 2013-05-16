package client;



import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import client.gui.UserGUI;
import exceptions.InvalidUsernameException;

/**
 * @category no_didit
 *
 */
public class ChatClientNoDiditTest {

    @Test(expected = InvalidUsernameException.class) 
    public void invalidUsernameTest() throws UnknownHostException, IOException {
        ChatClient c = new ChatClient();
        UserGUI gui = new UserGUI(c);
        c.attemptLogin("localhost", "4444", "6005isthebest", "green", gui);
    }

}
