package server;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import client.User;

public class ChatServerTest {

    //@Test
    /**
     * Tests a socket connecting to server and trying out different messages checking the correct response is received
     * @throws UnknownHostException
     * @throws IOException
     */
    public void compositionalTest() throws UnknownHostException, IOException {
        ChatServer.main(new String[] {"-p"," ","4444"});
        String connectInfo = "-l gomath black";
        Socket socket = new Socket("18.189.47.90", 4444);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        out.print(connectInfo);
        for (String line = in.readLine(); line!=null; line = in.readLine()) {
                    
                System.out.println(line);
            
        }
        socket.close();
        System.out.println("success?");
    } 
    
    @Test
    /**
     * Make sure string responses are appropriate
     */
    public void handleClientRequestTest() throws IOException, InterruptedException {
        ChatServer.main(new String[] {"-p"," ","4444"});
        //ChatServer chatty = new ChatServer(4444);
        //chatty.serve();
        TimeUnit.SECONDS.sleep(5);
        Socket socket = new Socket("18.189.47.90", 4444);
        ServerMessage message = ChatServer.handleClientRequest("-l gomath black",socket);
        System.out.println(message.getText());
        
    }
    

}
