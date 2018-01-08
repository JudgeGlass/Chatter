import com.JudgeGlass.Chatter.Client.Client;
import com.JudgeGlass.Chatter.Client.Notification;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String args[]) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("OS Name: " + System.getProperty("os.name"));
        System.out.println("OS arch: " + System.getProperty("os.arch"));

        if(!System.getProperty("java.version").contains("1.8")){
            JOptionPane.showMessageDialog(null, "Invalid Java version. You need Java 8(1.8.x)\nYou have " + System.getProperty("java.version"));
            return;
        }

        Client client = new Client();

    }
}
