package com.JudgeGlass.Chatter.Client;

import com.JudgeGlass.Chatter.misc.Utils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class ListHandler {
    public static void getServersFromFile(ArrayList<String> hosts, ArrayList<String> usernames, ArrayList<Integer> ports, DefaultListModel listModel){
        if(new File("Chatter_Save.dat").exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String fileName = "Chatter_Save.dat";

                    if (Utils.fileEmpty(fileName)) {
                        return;
                    }

                    int usernameStart = 1;
                    while (true) {
                        usernames.add(Utils.indexOf(Utils.readLine(fileName, usernameStart), '='));
                        if (usernameStart + 6 > Utils.getFileLineCount(fileName)) {
                            break;
                        }
                        usernameStart += 6;
                    }

                    int hostStart = 2;
                    while (true) {
                        hosts.add(Utils.indexOf(Utils.readLine(fileName, hostStart), '='));
                        if (hostStart + 6 > Utils.getFileLineCount(fileName)) {
                            break;
                        }
                        hostStart += 6;
                    }

                    int portStart = 3;
                    while (true) {
                        if (Utils.indexOf(Utils.readLine(fileName, portStart), '=').equals("## SERVER_END ##")) {
                            break;
                        }
                        ports.add(Integer.parseInt(Utils.indexOf(Utils.readLine(fileName, portStart), '=')));
                        if (portStart + 6 > Utils.getFileLineCount(fileName)) {
                            break;
                        }
                        portStart += 6;
                    }

                    for (int i = 0; i < hosts.size(); i++) {
                        listModel.addElement(hosts.get(i));
                    }
                }
            }).start();
        }
    }
}
