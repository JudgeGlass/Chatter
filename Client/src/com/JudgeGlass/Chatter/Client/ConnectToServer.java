package com.JudgeGlass.Chatter.Client;

import com.JudgeGlass.Chatter.Server.*;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;

public class ConnectToServer {
    private JDialog frame;
    private JPanel panel;

    private JButton btnOk;
    private JButton btnCancel;

    public static JTextField txtUsername;
    public static JTextField txtHost;
    public static JTextField txtPort;
    private static JPasswordField txtPassword;

    private JLabel lblUsername;
    private JLabel lblHost;
    private JLabel lblPort;
    private JLabel lblPassword;

    public ConnectToServer(){
        frame = new JDialog();
        frame.setTitle("Connect");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(225, 270));
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        frame.add(panel);

        init();
        frame.setVisible(true);
    }

    private void init(){
        JPopupMenu popup = new JPopupMenu();
        JMenuItem item = new JMenuItem(new DefaultEditorKit.CutAction());
        item.setText("Cut");
        popup.add(item);
        item = new JMenuItem(new DefaultEditorKit.CopyAction());
        item.setText("Copy");
        popup.add(item);
        item = new JMenuItem(new DefaultEditorKit.PasteAction());
        item.setText("Paste");
        popup.add(item);


        lblUsername = new JLabel("Username:");
        lblUsername.setBounds(5, 10, 150, 15);
        frame.getContentPane().add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(5, 25, 200, 27);
        txtUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        frame.getContentPane().add(txtUsername);

        lblPassword = new JLabel("Server Password:");
        lblPassword.setBounds(5, 55, 150, 15);
        frame.getContentPane().add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(5, 70, 200, 27);
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        frame.getContentPane().add(txtPassword);

        lblHost = new JLabel("IP/Hostname:");
        lblHost.setBounds(5, 100, 150, 15);
        frame.getContentPane().add(lblHost);

        txtHost = new JTextField();
        txtHost.setBounds(5, 115, 200, 27);
        txtHost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        frame.getContentPane().add(txtHost);

        lblPort = new JLabel("Port (1 - 65535):");
        lblPort.setBounds(5, 150, 150, 15);
        frame.getContentPane().add(lblPort);

        txtPort = new JTextField("6765");
        txtPort.setBounds(5, 165, 200, 27);
        txtPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        frame.getContentPane().add(txtPort);

        /*lblPortForwardNote = new JLabel("<html>NOTE: You must have this port<br>forwarded.</html>");
        lblPortForwardNote.setBounds(5, 155, 250, 30);
        frame.getContentPane().add(lblPortForwardNote);*/

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(5, 200, 90, 27);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.getContentPane().add(btnCancel);

        btnOk = new JButton("OK");
        btnOk.setBounds(115, 200, 90, 27);
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
        frame.getContentPane().add(btnOk);

        txtPort.setComponentPopupMenu(popup);
        txtHost.setComponentPopupMenu(popup);
        txtUsername.setComponentPopupMenu(popup);
    }

    private void connectToServer(){
        try {
            if(txtHost.getText().isEmpty() || txtPort.getText().isEmpty() || txtUsername.getText().isEmpty() || new String(txtPassword.getPassword()).isEmpty()){
                JOptionPane.showMessageDialog(null, "Please fill all text fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(txtUsername.getText().length() > 20){
                JOptionPane.showMessageDialog(null, "Username too long. Max length: 25 (" + txtUsername.getText().length() + ")", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //client.startRunning(txtHost.getText(), Integer.parseInt(txtPort.getText()), txtUsername.getText());
                    Client.clearChatWindow();
                    Client.startRunning(txtHost.getText(), Integer.parseInt(txtPort.getText()), txtUsername.getText(), new String(txtPassword.getPassword()));
                    frame.dispose();
                }
            }).start();
            frame.dispose();
        }catch (Exception e1){
            JOptionPane.showMessageDialog(null, "Error: " + e1, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void setPasswordFocused(){
        txtPassword.requestFocus();
    }
}
