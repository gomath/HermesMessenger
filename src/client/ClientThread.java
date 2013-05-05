package client;

import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable {
    Socket socket;
    
    public ClientThread(Socket socket){
        this.socket = socket;
    }
    
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
