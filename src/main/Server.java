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
        //default run it at port 4444
        ChatServer.main(new String[]{"-p", "4444"});
    }
}
