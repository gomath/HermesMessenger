package server;

import java.net.Socket;
import java.util.ArrayList;

import client.UserInfo;
/**
 * Object representing outgoing message from server
 *
 */
public class ServerMessage {
    private final ArrayList<Socket> recipients;
    private final String text;
    public ServerMessage(ArrayList<Socket> recipients, String text) {
        this.recipients = recipients;
        this.text = text + '\n';
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
            for (UserInfo ui : ChatServer.getInfoMap().values()) {
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
