package com.JudgeGlass.Chatter.misc;

import javax.swing.*;
import java.io.File;

public class MakeServer {
    public MakeServer(){
        if(!new File("HeadlessServer.jar").exists()) {
            Utils.extractInnerFiles("HeadlessServer.jar");
        }
        if(!new File("RunServer.exe").exists()){
            Utils.extractInnerFiles("RunServer.exe");
        }

        if(!new File("SERVER.jks").exists()){
            Utils.extractInnerFiles("SERVER.jks");
        }

        try {
            if(!System.getProperty("os.name").equals("Linux"))
                Runtime.getRuntime().exec("RunServer.exe");
            else
                JOptionPane.showMessageDialog(null, "In ther terminal, please run:\n'java -jar " + System.getProperty("user.dir") + "/HeadlessServer.jar");
        }catch (Exception e1){
            JOptionPane.showMessageDialog(null, "Could not run server.\n" + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
