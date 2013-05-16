package client.user;


/**
 * Object representing an instant message from one User to one or more other Users.
 *
 */
public class Message {
    private final UserInfo sender;
    private final Conversation convo;
    private final String text;
    
    /**
     * Creates the Message object.
     * @param sender User who sent the message
     * @param convo Conversation that the message belongs to
     * @param text actual String of what was sent
     */
    public Message(UserInfo sender, Conversation convo, String text) {
        this.sender = sender;
        this.convo = convo;
        this.text = text;
    }
    
    /**
     * Gets the sender of the message
     * @return the sender (UserInfo)
     */
    public UserInfo getSender() {
        return this.sender;
    }
    
    /**
     * Gets the conversation this message is part of
     * @return the conversation
     */
    public Conversation getConvo() {
        return this.convo;
    }
    
    /**
     * Gets the text of the message
     * @return the text
     */
    public String getText() {
        return this.text;
    }
}
