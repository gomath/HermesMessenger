package client;

public class LoginThread implements Runnable {
        private Object lock;
        public LoginThread(Object lock){
            this.lock = lock;
        
        }
        
        public void run(){
            synchronized(lock){
                while(true){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }           
        }
}