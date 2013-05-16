package client.user;

import java.net.Socket;

/**
 * Object to hold information about other users, specifically their username, 
 * desired color, and the Socket through which to reach them.
 *
 */
public class UserInfo {
    private final String username;
    private final String color;
    private final Socket socket;
    /**
     * Constructor for use by the server to hold information about
     * connected clients, including socket where they can be contacted.
     * @param username the username of the connected user
     * @param color the color associated with that user
     * @param socket the socket where they can be reached
     */
    public UserInfo(String username, String color, Socket socket) {
        this.username = username;
        this.color = color;
        this.socket = socket;
    }
    /**
     * Constructor for use of other clients to keep track of other
     * connected users
     * @param username the username of the user.
     * @param color the color associated with that user
     */
    public UserInfo(String username, String color) {
        this.username = username;
        this.color = color;
        this.socket = null;
    }
    /**
     * Gets the username for this user
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }
    /**
     * Gets the color for this user as a string
     * @return the color
     */
    public String getColor() {
        return this.color;
    }
    /**
     * Gets the socket where this user can be reached
     * @return socket where user is connected, or null if called
     * from another client (which doesn't have that information)
     */
    public Socket getSocket() {
        return this.socket;
    }
}
