package server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * creates new thread to run the server
 *
 */
public class ChatServerThread extends Thread {
    Socket socket;
    ChatServer server;
    
    /**
     * Make the thread
     * @param socky the Socket the server uses to communicate
     * @param servy the ChatServer that made the thread
     */
    public ChatServerThread(Socket socky, ChatServer servy) {
        socket = socky;
        server = servy;
    }
    
    /**
     * handles socket connection for the server
     */
    @Override
    public void run() {
        
        try {
            server.handleConnection(socket);
        } catch (SocketException e) {
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

}
