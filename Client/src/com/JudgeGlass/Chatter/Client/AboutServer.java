package com.JudgeGlass.Chatter.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class AboutServer {
    private Socket connectedSocket;

    private JDialog frame;
    private JPanel panel;
    private JLabel lblHostname;
    private JLabel lblPort;
    private JLabel lblrandom;
    private JButton btnOk;

    public AboutServer(Socket s){
        connectedSocket = s;
        frame = new JDialog();
        frame.setTitle("About Server");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(200, 150);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        panel = new JPanel();
        frame.add(panel);

        init();
        frame.setVisible(true);
    }

    private void init(){
        if(connectedSocket != null) {
            lblHostname = new JLabel("Hostname: " + connectedSocket.getInetAddress().getHostName());
            lblHostname.setBounds(5, 5, 200, 15);

            lblPort = new JLabel("Port: " + connectedSocket.getPort());
            lblPort.setBounds(5, 25, 150, 15);

            lblrandom = new JLabel("<html>Remote Address: <br>" + connectedSocket.getRemoteSocketAddress().toString()+"</html>");
            lblrandom.setBounds(5, 45, 250, 30);
        }else{
            lblHostname = new JLabel("Hostname: Not Connected");
            lblHostname.setBounds(5, 5, 200, 15);

            lblPort = new JLabel("Port: Not Connected");
            lblPort.setBounds(5, 25, 150, 15);

            lblrandom = new JLabel("Remote Address: Not Connected");
            lblrandom.setBounds(5, 45, 250, 30);
        }

        btnOk = new JButton("OK");
        btnOk.setBounds(25, 80, 100, 25);
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.getContentPane().add(lblHostname);
        frame.getContentPane().add(lblPort);
        frame.getContentPane().add(lblrandom);
        frame.getContentPane().add(btnOk);
    }
}
