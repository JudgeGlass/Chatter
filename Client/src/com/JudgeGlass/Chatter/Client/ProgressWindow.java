package com.JudgeGlass.Chatter.Client;

import javax.swing.*;

public class ProgressWindow {
    private JFrame frame;
    private JPanel panel;
    private JProgressBar bar;
    private JLabel lblInfo;

    public ProgressWindow(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setTitle("Connecting...");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setSize(200, 100);

        panel = new JPanel();
        frame.add(panel);

        init();
        frame.setVisible(true);
    }

    private void init(){
        lblInfo = new JLabel("Connecting...");
        lblInfo.setBounds(5, 5, 150, 15);
        frame.getContentPane().add(lblInfo);

        bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setBounds(5, 25, 170, 30);
        frame.getContentPane().add(bar);
    }

    public void closeWindow(){
        frame.dispose();
    }
}
