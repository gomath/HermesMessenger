package server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ChatServerThread extends Thread {
    Socket socket;
    
    public ChatServerThread(Socket socky) {
        socket = socky;
    }
    

    @Override
    public void run() {
        System.out.println("running");
        try {
            ChatServer.handleConnection(socket);
        } catch (SocketException e) {
            return;
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
