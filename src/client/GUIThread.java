package client;


public class GUIThread implements Runnable {
    
    public GUIThread(){
    }
    
    public void run(){
        new UserGUI();
    }
    
}