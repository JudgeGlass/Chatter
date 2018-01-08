package com.JudgeGlass.Chatter.Client;

import javax.swing.*;
import java.io.File;
import java.io.PrintWriter;

public class SaveFile {
    public void saveText(String text){
        SaveFileDialog saveFileDialog = new SaveFileDialog();
        saveFileDialog.save(null);
        String dir = saveFileDialog.getDir();
        try{
            if(new File(dir).exists()){
                JOptionPane.showMessageDialog(null, "File already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!dir.isEmpty()) {
                PrintWriter writer = new PrintWriter(new File(dir + ".txt"), "UTF-8");
                writer.write(text);
                writer.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
