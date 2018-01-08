package com.JudgeGlass.Chatter.Client;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SaveFileDialog {
    private String dir = "";
    public void save(final JPanel panel) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setDialogTitle("Choose Directory To Save To");
        //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setApproveButtonText("Save");

        javax.swing.filechooser.FileFilter filter = new FileNameExtensionFilter("Text File", "txt");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            dir = chooser.getSelectedFile().toString();
        } else {
            System.err.println("Could not open Dir");
            return;
        }
    }
    public String getDir() {
        return dir;
    }
}