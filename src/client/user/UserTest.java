package client.user;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

public class UserTest {
    @Test
    public void test() {
        String username1 = "jenn";
        String color1 = "orange";
            Socket socket1 = new Socket();
            User user = new User(username1, color1, socket1);
            assertEquals(user.getUsername(), username1);

            User user2 = new User("dan", color1, socket1);

            System.out.println(user.getUsername());
            System.out.println(user2.getUsername());
        
            
        }
    }
    
/*
 * Method: User(String username1, String color1, Socket socket1)
Strategy: est creation of a User with mock socket and ensure things are set correctly
Method: handleRequest(String input)
Strategy: partition the input space and test all possible inputs that do not affect the GUI and use Integration testing with ConversationView
Method: sendMessageToServer(String text)
Unit test by having something listening to a mock socket output
Method: startConvo(Object[] usernames) 
Strategy: unit test that tests the correct message is returned when making a convo from username objects
Method: closeConvo(Conversation convo)
Strategy: unit test that the right message is sent from the socket given a conversation
Method: updateConvo(String input)
Strategy: this involves updating the ConversationView so must be Integration tested with ConversationView
Method: login()
Strategy: make sure appropriate message sent to socket outputstream 
Method: quit()
Strategy: since this accesses the UserGUI, shoiuld be tested with Integration tests with the GUI
Method: setActiveConvo(Conversation convo)
Strategy: make sure the active convo is set to the correct Conversation
Method: setOnlineUsers(ConcurrentHashMap<String,UserInfo> userMap)
Strategy: make sure correct users are set
Method: addOnlineUser(UserInfo user)
Strategy: ensure users added correctly
Method: removeOnlineUser(String user)
Strategy: test that user can be removed from online user list properly
Method: getUsername()
Strategy: assert correct username returned
Method: getColor()
Strategy: assert correct color returned
Method: getSocket()
Strategy: assert correct socket instance returned
Method: getOnlineUsers()
Strategy: assert that online users returned
Method: getMyConvos()
Strategy: assert correct conversations returned
Method: addNewMyConvo(Conversation convo)
Strategy: since this accesses ConversationView needs to be Integration tested with ConversationView
Method: removeMyConvo(Conversation convo)
Strategy: make sure correct conversation removed from convos
Method: checkDuplicateConvo(Conversation convo)
Strategy: unit test to test expected DuplicateConvoException and to test unique conversation passes (partitioning input space)

 */



