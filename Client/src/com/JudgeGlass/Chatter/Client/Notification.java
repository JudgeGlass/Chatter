package com.JudgeGlass.Chatter.Client;

import java.awt.*;

public class Notification {
    public static void showNotification(String sender, String message) throws AWTException {
        if(SystemTray.isSupported()) {
            //Obtain only one instance of the SystemTray object
            SystemTray tray = SystemTray.getSystemTray();

            //If the icon is a file
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            //Alternative (if the icon is on the classpath):
            //Image image = Toolkit.getToolkit().createImage(getClass().getResource("icon.png"));
            TrayIcon trayIcon = new TrayIcon(image, "Chatter");
            //Let the system resizes the image if needed
            trayIcon.setImageAutoSize(true);
            //Set tooltip text for the tray icon
            trayIcon.setToolTip(sender + ":" + message);
            tray.add(trayIcon);
            trayIcon.displayMessage(sender, message, TrayIcon.MessageType.INFO);
        }
    }
}
