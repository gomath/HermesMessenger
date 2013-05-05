package main;

import server.ChatServer;

/**
 * Chat server runner.
 */
public class Server {

    /**
     * Start a chat server.
     */
    public static void main(String[] args) {
        ChatServer.main(new String[]{"-p", "4444"});
    }
}
