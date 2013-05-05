package client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Conversation {
    private final String convoID;
    private final ConcurrentHashMap<String, UserInfo> participants;
    private ArrayList<Message> history;
    
    /**
     * Construct a conversation object based on information about
     * its participants.
     * @param participants a map from the String usernames of the participants
     * to their corresponding UserInfo objects.
     */
    public Conversation(ConcurrentHashMap<String, UserInfo> participants){
        this.convoID = alphabetizeHashMap(participants);
        this.participants = participants;
        this.history = new ArrayList<Message>();
    }
    
    /**
     * Add a message to the conversation history.
     * @param msg the Message to be added
     */
    public void addMessage(Message msg){
        this.history.add(msg);
    }
    
    /**
     * Getter methods for various private instance variables.
     * @return the instance variable.
     */
    public List<Message> getMessages(){
        return this.history;
    }
    
    public String getConvoID(){
        return this.convoID;
    }
    
    public ConcurrentHashMap<String, UserInfo> getParticipantsMap(){
        return this.participants;
    }
    
    /**
     * Alphabetize the participants of a Conversation to get their
     * convoID
     * @param participants, a HashMap from String usernames to corresponding
     * UserInfo objects. 
     * @return a String that is an alphabetized list of the usernames,
     * with spaces in between each.
     */
    public String alphabetizeHashMap(ConcurrentHashMap<String, UserInfo> participants){
        List<String> userList = new ArrayList<String>();
        for(String name : participants.keySet()){
            userList.add(name);
        }
        java.util.Collections.sort(userList); //alphabetize
        StringBuilder convoID = new StringBuilder();
        for(String userName : userList){
            convoID.append(userName);
            convoID.append(' ');
        }
        return convoID.toString();
    }
}
