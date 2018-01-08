package com.JudgeGlass.Chatter.Client;

import com.JudgeGlass.Chatter.misc.Utils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.*;

public class HelpWindow {
    private JFrame frame;
    private JTextArea txtArea;

    public HelpWindow() {
        frame = new JFrame();
        frame.setTitle("Help");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        frame.setAlwaysOnTop(true);

        initialize();
    }

    private void initialize() {
        txtArea = new JTextArea();
        txtArea.setFont(new Font(Font.SANS_SERIF, 0, 12));
        txtArea.setEditable(false);
        JScrollPane sp = new JScrollPane(txtArea);
        frame.getContentPane().add(sp, BorderLayout.CENTER);

        txtArea.append(new Utils().getHelpFile());
        txtArea.setCaretPosition(0);
    }

    public void setVisible(final boolean vis) {
        frame.setVisible(vis);
    }
}