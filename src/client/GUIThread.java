package client;

import java.io.IOException;
import java.net.Socket;

public class GUIThread implements Runnable {
    
    public GUIThread(){
    }
    
    public void run(){
        new UserGUI();
    }
    
}