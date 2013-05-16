package server;

import java.net.Socket;
import java.util.ArrayList;

import client.user.UserInfo;
/**
 * Object representing outgoing message from server, which can
 * go to many different Socket recipients
 *
 */
public class ServerMessage {
    private final ArrayList<Socket> recipients;
    private final String text;
    private final ChatServer server;
    
    /**
     * Make the ServerMessage
     * @param recipients the Users to send this message to
     * @param text the text of the message
     * @param server the ChatServer that the message should be sent through
     */
    public ServerMessage(ArrayList<Socket> recipients, String text, ChatServer server) {
        this.recipients = recipients;
        this.text = text + '\n';
        this.server = server;
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
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TO: ");
        for (Socket s : recipients) {
            for (UserInfo ui : server.getInfoMap().values()) {
                if (ui.getSocket() == s) {
                    sb.append(ui.getUsername());
                    sb.append(" ");
                }
            }
        }
        sb.append("TEXT: ");
        sb.append(this.getText());
        return sb.toString();
    }
}
