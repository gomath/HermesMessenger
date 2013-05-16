package client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import exceptions.InvalidUsernameException;
/**
 * Creates the login view that initiates a session for a user
 *
 */
public class LoginView extends JFrame{
    private static final long serialVersionUID = 1L;
    private final JTextField ipAddress;
    private final JTextField portNumber;
    private final JTextField username;
    private final JComboBox colorDropDown;
    private final JButton submitButton;
    private final JLabel hermes;
    private final JLabel messenger;
    
    /**
     * Creates the view and fills with the appropriate content
     */
    public LoginView(final UserGUI gui) {
        setTitle("Hermes Messenger Login");
        setBackground(new Color(96, 80, 220));
        setPreferredSize(new Dimension(300,300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //HERMES
        hermes = new JLabel("HERMES");
        hermes.setForeground(Color.white);
        hermes.setAlignmentX(CENTER_ALIGNMENT);
        hermes.setAlignmentY(CENTER_ALIGNMENT);
        hermes.setFont(new Font("Serif", Font.BOLD, 50));
        messenger = new JLabel("MESSENGER");
        messenger.setForeground(Color.black);
        messenger.setAlignmentY(CENTER_ALIGNMENT);
        messenger.setFont(new Font("SansSerif", Font.PLAIN, 25));

        //IP ADDRESS FIELD
        ipAddress = new JTextField();
        ipAddress.setText("IP Address");
        ipAddress.setMaximumSize(new Dimension(200, 1));
        //when clicked away from
        ipAddress.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent arg0) {}
            @Override
            public void focusLost(FocusEvent arg0) {
                if (ipAddress.getText().equals("")) {
                    ipAddress.setText("IP Address");
                }
            }
        });
        //when user starts typing
        ipAddress.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (ipAddress.getText().equals("IP Address")) {
                    ipAddress.setText("");                
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        
        //PORT NUMBER
        portNumber = new JTextField();
        portNumber.setText("Port");
        portNumber.setMaximumSize(new Dimension(200, 1));
        //when clicked away from
        portNumber.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent arg0) {}
            @Override
            public void focusLost(FocusEvent arg0) {
                if (portNumber.getText().equals("")) {
                    portNumber.setText("Port");
                }
            }
        });
        //when user starts typing
        portNumber.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (portNumber.getText().equals("Port")) {
                    portNumber.setText("");                
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        
        //USERNAME
        username = new JTextField();
        username.setText("Username");
        username.setMaximumSize(new Dimension(200, 1));
        //when clicked away from
        username.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent arg0) {}
            @Override
            public void focusLost(FocusEvent arg0) {
                if (username.getText().equals("")) {
                    username.setText("Username");
                }
            }
        });
        //when user starts typing
        username.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (username.getText().equals("Username")) {
                    username.setText("");
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        
        //COLOR PICKER
        String[] colorStrings = {"red", "orange", "yellow", "green", "blue", "pink"};
        colorDropDown = new JComboBox(colorStrings);
        colorDropDown.setSelectedIndex(0);
        DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
        dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        colorDropDown.setRenderer(dlcr);
        colorDropDown.setMaximumSize(new Dimension(300, 1));
        
        //SUBMIT BUTTON
        submitButton = new JButton();
        submitButton.setText("Submit");
        //when enter is hit or submit is clicked
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    gui.getClient().attemptLogin(ipAddress.getText(), portNumber.getText(), username.getText(), (String) colorDropDown.getSelectedItem(), gui);
                    gui.setUserView();
                    gui.getClient().runUser();
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid port number");
                    portNumber.setText("Port");
                } catch (UnknownHostException e2) {
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid IP Address");
                    ipAddress.setText("IP Address");
                } catch (InvalidUsernameException e3) {
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid Username");
                    username.setText("Username");
                } catch (ConnectException e4) {
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid IP Address");
                    ipAddress.setText("IP Address");
                } catch (Exception e5) {
                    JOptionPane.showMessageDialog(getContentPane(), e5.getStackTrace());
                    ipAddress.setText("IP Address");
                    portNumber.setText("Port");
                    username.setText("Username");
                }
            }
        });
        //GROUP LAYOUT
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        //HORIZONTAL GROUPINGS
        System.out.println("grouping: " + Thread.currentThread().getId());
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(hermes)
                .addComponent(messenger)
                .addComponent(ipAddress)
                .addComponent(portNumber)
                .addComponent(username)
                .addComponent(colorDropDown)
                .addComponent(submitButton));
        //VERTICAL GROUPINGS
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(hermes)
                .addComponent(messenger)
                .addComponent(ipAddress)
                .addComponent(portNumber)
                .addComponent(username)
                .addComponent(colorDropDown)
                .addComponent(submitButton));
        getRootPane().setDefaultButton(submitButton);
        pack();

        setVisible(true);
    }
    
    /**
     * closes the frame
     */
    public void close() {
        //set visibility to false, and dispose
        this.setVisible(false);
        this.dispose();
         
    }
    
    /**
     * Makes a new LoginView and sets gui's LoginView to be it
     * @param gui, the UserGUI that controls the LoginView
     */
    public static void main(final UserGUI gui) {
        LoginView login = new LoginView(gui);
        gui.setLoginView(login);        
    }


}
