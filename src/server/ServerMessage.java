package server;

import java.net.Socket;
import java.util.ArrayList;
/**
 * Object representing outgoing message from server
 *
 */
public class ServerMessage {
    private final ArrayList<Socket> recipients;
    private final String text;
    public ServerMessage(ArrayList<Socket> recipients, String text) {
        this.recipients = recipients;
        this.text = text;
    }
    /**
     * Gets the recipient sockets of the message
     * @return recipients
     */
    public ArrayList<Socket> getRecipients() {
        return this.recipients;
    }
    /**
     * Gets the text of message
     * @return text
     */
    public String getText() {
        return this.text;
    }
}
