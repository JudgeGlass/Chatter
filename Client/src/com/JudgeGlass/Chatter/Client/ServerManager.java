package com.JudgeGlass.Chatter.Client;

import javax.swing.*;

public class ServerManager {
    public static String[] addServer(){
        String returnR[] = {"NULL"};
        String username = JOptionPane.showInputDialog(null, "Username:", "Custom username", JOptionPane.QUESTION_MESSAGE);
        if(username == null){return returnR;}
        String hostname = JOptionPane.showInputDialog(null, "Hostname/IP:", "Server Hostname / IP", JOptionPane.QUESTION_MESSAGE);
        if(hostname == null){return returnR;}
        String port = JOptionPane.showInputDialog(null, "Port:", "Server port", JOptionPane.QUESTION_MESSAGE);
        if(port == null){return returnR;}
        String results[] = {username, hostname, port};
        return results;
    }
}
