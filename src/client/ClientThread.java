package client;

import java.io.IOException;
import java.net.Socket;

import client.user.User;
/**
 * Thread run when a new client is connected
 *
 */
public class ClientThread implements Runnable {
    Socket socket;
    
    public ClientThread(Socket socket){
        this.socket = socket;
    }
    
    /**
     * handles the socket connection
     */
    public void run(){
        System.out.println("Created new ClientThread");
        try {
            User.handleConnection(socket);
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
