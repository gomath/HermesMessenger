package client;

import java.io.IOException;
import java.net.Socket;

import client.user.User;
/**
 * Thread to run when a new client is connected
 *
 */
public class ClientThread implements Runnable {
    Socket socket;
    User user;
    /**
     * Creates the thread
     * @param socket the Socket to connect to
     * @param user the User associated with the client
     */
    public ClientThread(Socket socket, User user){
        this.socket = socket;
        this.user = user;
    }
    
    /**
     * handles the socket connection
     */
    public void run(){
        try {
            user.handleConnection(socket);
        } catch (Exception e) {
            e.printStackTrace(); 
        } finally {
            try{
                socket.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    
}
