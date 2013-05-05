package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.UnknownHostException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import exceptions.InvalidUsernameException;

public class LoginView extends JFrame{
    private final JTextField ipAddress;
    private final JTextField portNumber;
    private final JTextField username;
    private final JComboBox colorDropDown;
    private final JButton submitButton;
    /*
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
    */
    
    public LoginView() {
        setPreferredSize(new Dimension(600,400));
    
        //IP ADDRESS FIELD
        ipAddress = new JTextField();
        ipAddress.setText("IP Address");
        ipAddress.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent arg0) {
                ipAddress.setText("");
            }
            @Override
            public void focusLost(FocusEvent arg0) {
                if (ipAddress.getText().equals("")) {
                    ipAddress.setText("IP Address");
                }
            }
        });
        
        //PORT NUMBER
        portNumber = new JTextField();
        portNumber.setText("Port");
        portNumber.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent arg0) {
                portNumber.setText("");
            }
            @Override
            public void focusLost(FocusEvent arg0) {
                if (portNumber.getText().equals("")) {
                    portNumber.setText("Port");
                }
            }
        });
        
        
        //USERNAME
        username = new JTextField();
        username.setText("Username");
        username.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent arg0) {
                username.setText("");
            }
            @Override
            public void focusLost(FocusEvent arg0) {
                if (username.getText().equals("")) {
                    username.setText("Username");
                }
            }
        });
        
        //COLOR PICKER
        String[] colorStrings = {"red", "orange", "yellow", "green", "blue", "pink"};
        colorDropDown = new JComboBox(colorStrings);
        colorDropDown.setSelectedIndex(4);
        
        //SUBMIT BUTTON
        submitButton = new JButton();
        submitButton.setText("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    Client.attemptLogin(ipAddress.getText(), portNumber.getText(), username.getText(), (String) colorDropDown.getSelectedItem());
                } catch (NumberFormatException e1) {
                    //INVALID PORT NUMBER
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid port number");
                    portNumber.setText("Port");
                } catch (UnknownHostException e2) {
                    //INVALID IP
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid IP Address");
                    ipAddress.setText("IP Address");
                } catch (InvalidUsernameException e3) {
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid Username");
                    username.setText("Username");
                } catch (Exception e4) {
                    JOptionPane.showMessageDialog(getContentPane(), e4);
                    ipAddress.setText("IP Address");
                    portNumber.setText("Port");
                    username.setText("Username");
                } finally {

                }
            }
        });
        
        //GROUP LAYOUT
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        //HORIZONTAL GROUPINGS
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(ipAddress)
                .addComponent(portNumber)
                .addComponent(username)
                .addComponent(colorDropDown)
                .addComponent(submitButton));
        //VERTICAL GROUPINGS
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(ipAddress)
                .addComponent(portNumber)
                .addComponent(username)
                .addComponent(colorDropDown)
                .addComponent(submitButton));
        getRootPane().setDefaultButton(submitButton);
        pack();
    }
    
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginView main = new LoginView();
                main.setVisible(true);
            }
        });
    }
}
