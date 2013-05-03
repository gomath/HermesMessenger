package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class UserGUI  extends JFrame {
    private User myUser;
    // remember to use these objects in your GUI:
    private final JButton connectButton;
    private final JTextField connection;
    private final JLabel connectionInfo;
    public final JTextField newMessage;
    private final JTable messages;
    private final JLabel messageLabel;
    private String hostname;
    private String port;
    private DefaultTableModel model;

    public UserGUI() {
        setPreferredSize(new Dimension(800,800));
        connectButton = new JButton("Connect");
        connectButton.setName("StartConnection");
        connection = new JTextField();
        connection.setName("connectionInfo");
        connectionInfo = new JLabel("Enter Hostname space port space username color");
        connectionInfo.setName("connectionInfo");
        newMessage = new JTextField();
        newMessage.setName("newMessage");
        messageLabel = new JLabel("Type a message here:");
        messageLabel.setName("messageLabel");
        
        model = new DefaultTableModel();
        messages = new JTable(model);
        messages.setName("guessTable");
        messages.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        messages.setGridColor(Color.black);
        
        model.addColumn("1");
        model.addColumn("2");
        //label the frame
        setTitle("SimpleChat(TM)");
        
        //use GroupLayout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        //auto gaps and container gaps
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        //horizontal group for GroupLayout
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(connectionInfo)
                            .addComponent(connectButton)
                            .addComponent(connection))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(messageLabel)
                            .addComponent(newMessage))
                    .addComponent(messages)
                   
        );
        
        //vertical group for GroupLayout
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(connectionInfo)
                            .addComponent(connectButton)
                            .addComponent(connection))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(messageLabel)
                            .addComponent(newMessage))
                    .addComponent(messages)
        );
        
        pack();
        
        //add listeners for user input
        /**
         * listens for enter key pressed in newPuzzleNumber field
         * sets puzzle number to input or if input is not a positive integer, to random positive integer
         */
        connection.addKeyListener(new KeyAdapter() {
            public synchronized void keyPressed(KeyEvent event) {
                //respond only to enter
                if(event.getKeyCode() == KeyEvent.VK_ENTER) {
                    
                    //start a new puzzle
                    try {
                        newUser();
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                 
                }
            }
        });
        
        /**
         * listens for button press of newPuzzleButton
         * sets puzzle number to input or if input is not a positive integer, to random positive integer
         */
        connectButton.addMouseListener(new MouseAdapter() {
            public synchronized void mouseClicked(MouseEvent event) {
                
                //Start a new puzzle
                try {
                    newUser();
                } catch (NumberFormatException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               
            }
        });
    }
        
        /*
        newMessage.addKeyListener(new KeyAdapter() {
            public synchronized void keyPressed(KeyEvent event) {
                //make a guess if enter key is hit
                if(event.getKeyCode() == KeyEvent.VK_ENTER) {
                    String guessedWord = newMessage.getText();
                  //clear field
                    newMessage.setText("");
                                        
                    String[] row = new String[] {guessedWord, "", ""};
                    
                    model.addRow(row);
                    int rowNum = model.getRowCount()-1;
                    
                    BackgroundQuery query = new BackgroundQuery(guessedWord, rowNum);
                    queryList.add(query);
                    query.execute();

                }
            }
        });
        */
    
    /**
     * creates a new puzzle either from the newPuzzleNumber field or randomly
     * @throws IOException 
     * @throws UnknownHostException 
     * @throws NumberFormatException 
     */
    private synchronized void newUser() throws NumberFormatException, UnknownHostException, IOException {
        String connectInfo = connection.getText();
        String[] connectInf = connectInfo.split(" ");
        Socket socket = new Socket(connectInf[0], Integer.parseInt(connectInf[1]));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        out.print("-l "+connectInf[2] + " "+connectInf[3]);
        for (String line = in.readLine(); line!=null; line = in.readLine()) {
            String[] response = line.split(" ");
            if(response[0] == "-f") {
                myUser = new User(connectInf[2], Color.getColor(connectInf[3]), socket);
                connection.setText("success!");
                break;
            }
            else {
                socket.close();
        }
        
        
    }
    
/*
    
    /**
     * Updates the GUI with the result of a guess and prints the server message or appropriate message
     * to Standard Output
     * @param row, int representing which row the guess result will be displayed
     * @param updatedData, ArrayList of string response components from server query
     */
        /*
    private synchronized void sendOutput(int row, ArrayList<String> updatedData) {
        
        //update the gui
        for(int i = 0; i < updatedData.size(); i++) {
            model.setValueAt(updatedData.get(i),row,i+1);
        }
        
        //print server response to Standard Out
        StringBuilder sb = new StringBuilder();
        if(Character.isDigit(updatedData.get(0).charAt(0))) sb.append("guess ");
        for(String s: updatedData) {
            sb.append(s);
            sb.append(' ');
        }
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
            
        
        
        return;
        
    }
    
    */
    }

    /**
     * Runs the GUI
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UserGUI main = new UserGUI();

                main.setVisible(true);
            }
        });
    }
}



