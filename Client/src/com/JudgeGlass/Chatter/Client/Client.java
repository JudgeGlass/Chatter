package com.JudgeGlass.Chatter.Client;

import com.JudgeGlass.Chatter.misc.MakeServer;
import com.JudgeGlass.Chatter.misc.Utils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;

import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultEditorKit;

public class Client {
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JTextField userText;
    private static JTextArea chatWindow;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;

    private static String message = "";
    private static String serverIP;

    private static int port = 6765;
    private static String serverPas;

    private static SSLSocket connection;
    private static String clientUname;
    private static boolean isConnected;
    private static boolean isMinimized = false;

    private static JMenuItem connect;
    private static JMenuItem disconnect;

    private static JPanel serverPanel;
    private static JButton btnAddServer;
    private static DefaultListModel listModel;
    private static JList serverList;
    private static JButton btnConnect;
    private static JMenuItem delete;
    private static JMenuItem delItem;
    private static JMenuItem connectItem;

    private static ArrayList<String> hosts = new ArrayList<>();
    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<Integer> ports = new ArrayList<>();
    private static SaveServers saveServers;

    public Client(){
        frame = new JFrame();

        frame.setTitle("Chat - " + serverIP + ":" + port);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(isConnected) {
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
                    Date date = new Date();

                    String exitCode = clientUname + "(" + dateFormat.format(date) + ") --> exit";
                    closeAll();
                    //sendMessage(exitCode);
                    frame.dispose();
                    System.exit(0);
                }else{
                    System.exit(-1);
                }
            }
        });

        mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);
        setMenuBar();
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!userText.getText().isEmpty()) {
                    if(userText.getText().length() <= 2000) {
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }else{
                        showMessage("\n Too many characters(" + userText.getText().length() + "). MAX: 2000");
                    }
                }
            }
        });

        /// COPY And PASTE right click ///
        JPopupMenu popup = new JPopupMenu();
        JMenuItem item = new JMenuItem(new DefaultEditorKit.CutAction());
        item.setText("Cut");
        popup.add(item);
        item = new JMenuItem(new DefaultEditorKit.CopyAction());
        item.setText("Copy");
        popup.add(item);
        item = new JMenuItem(new DefaultEditorKit.PasteAction());
        item.setText("Paste");
        popup.add(item);
        userText.setComponentPopupMenu(popup);
        //////////////////////////////////////

        JPopupMenu listPop = new JPopupMenu();
        delItem = new JMenuItem();
        delItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteServer();
            }
        });
        delItem.setText("Delete");
        listPop.add(delItem);
        connectItem = new JMenuItem();
        connectItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serverList.isSelectionEmpty() && !isConnected) {
                    actionConnect();
                }
            }
        });
        connectItem.setText("Connect");
        listPop.add(connectItem);


        JPanel chatArea = new JPanel(new BorderLayout());
        mainPanel.add(chatArea, BorderLayout.CENTER);
        chatArea.add(userText, BorderLayout.SOUTH);

        chatWindow = new JTextArea();
        chatWindow.setEditable(false);
        //chatWindow.setLineWrap(true);
        chatWindow.setComponentPopupMenu(popup);
        chatArea.add(new JScrollPane(chatWindow), BorderLayout.CENTER);

        serverPanel = new JPanel(new BorderLayout());
        //serverPanel.setLayout(new BoxLayout(serverPanel, BoxLayout.Y_AXIS));
        mainPanel.add(serverPanel, BorderLayout.LINE_START);

        btnAddServer = new JButton("Add Server");
        btnAddServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverInfo[] = ServerManager.addServer();
                if(serverInfo[0].equals("NULL")){return;}
                saveServers.addServer(serverInfo[0], serverInfo[1], Integer.parseInt(serverInfo[2]));
                usernames.add(serverInfo[0]);
                hosts.add(serverInfo[1]);
                ports.add(Integer.parseInt(serverInfo[2]));
                listModel.addElement(serverInfo[1]);
                saveServers.saveConfig("Chatter_Save.dat");
                int connectToServerNow = JOptionPane.showConfirmDialog(null, "Would you like to connect to the server?", "Connect Now?",
                        JOptionPane.YES_NO_OPTION);
                if(connectToServerNow == JOptionPane.YES_OPTION){
                    ConnectToServer connectToServer = new ConnectToServer();
                    connectToServer.txtUsername.setText(serverInfo[0]);
                    connectToServer.txtHost.setText(serverInfo[1]);
                    connectToServer.txtPort.setText(serverInfo[2]);
                    connectToServer.setPasswordFocused();
                }
            }
        });
        serverPanel.add(btnAddServer, BorderLayout.PAGE_START);

        listModel = new DefaultListModel();
        serverList = new JList(listModel);
        serverList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        serverList.setLayoutOrientation(JList.VERTICAL);
        serverList.setFixedCellWidth(150);
        serverList.setFixedCellHeight(15);
        JScrollPane scrollPaneList = new JScrollPane(serverList);
        serverPanel.add(scrollPaneList, BorderLayout.CENTER);
        serverList.setComponentPopupMenu(listPop);

        /** Checks to see if an server is selected*/
        serverList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(serverList.isSelectionEmpty()) {
                    btnConnect.setEnabled(false);
                    delete.setEnabled(false);
                    connectItem.setEnabled(false);
                }
                else {
                    if(!isConnected) {
                        btnConnect.setEnabled(true);
                        delete.setEnabled(true);
                        connectItem.setEnabled(true);
                    }
                }
            }
        });
        serverList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if(!isConnected) {
                        actionConnect();
                    }
                }
            }
        });

        btnConnect = new JButton("Connect");
        btnConnect.setEnabled(false);
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionConnect();
            }
        });
        serverPanel.add(btnConnect, BorderLayout.PAGE_END);

        frame.setSize(new Dimension(1000, 600));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        saveServers = new SaveServers();

        showMessage("To connect to a server, press 'Add Server'. Then press connect.\n");
        showMessage("To make a server, go to 'Server > Make Server'\n");


        ListHandler.getServersFromFile(hosts, usernames, ports, listModel);
    }

    private static void setMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        mainPanel.add(menuBar, BorderLayout.PAGE_START);

        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenu server = new JMenu("Server");
        menuBar.add(server);

        JMenu view = new JMenu("View");
        menuBar.add(view);

        JMenu help = new JMenu("Help");
        menuBar.add(help);

        connect = new JMenuItem("Direct Connect");
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectToServer connectToServer = new ConnectToServer();
            }
        });

        disconnect = new JMenuItem("Disconnect");
        disconnect.setEnabled(false);
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("exit");
                closeAll();
                disconnect.setEnabled(false);
            }
        });

        JMenuItem save = new JMenuItem("Save Chat");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveFile saveFile = new SaveFile();
                saveFile.saveText(chatWindow.getText());
            }
        });

        delete = new JMenuItem("Delete");
        delete.setEnabled(false);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteServer();
            }
        });

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isConnected){
                    closeAll();
                    frame.dispose();
                    System.exit(0);
                }else {
                    frame.dispose();
                    System.exit(-1);
                }
            }
        });

        JMenuItem aboutServer = new JMenuItem("About Server");
        aboutServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutServer s = new AboutServer(connection);
            }
        });

        JMenuItem makeServer = new JMenuItem("Make Server");
        //makeServer.setEnabled(false);
        makeServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MakeServer();
            }
        });

        JMenuItem getHelp = new JMenuItem("Get Help");
        getHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HelpWindow helpWindow = new HelpWindow();
                helpWindow.setVisible(true);
            }
        });
        help.add(getHelp);
        help.addSeparator();

        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutWindow();
            }
        });
        help.add(about);

        JCheckBoxMenuItem hide = new JCheckBoxMenuItem("Hide Connection Area");
        hide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hide.isSelected()){
                    serverPanel.setVisible(false);
                }else{
                    serverPanel.setVisible(true);
                }
            }
        });

        JCheckBoxMenuItem lineWrap = new JCheckBoxMenuItem("Wrap Text");
        lineWrap.setState(true);
        lineWrap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lineWrap.isSelected()){
                    chatWindow.setLineWrap(true);
                }else{
                    chatWindow.setLineWrap(false);
                }
            }
        });

        JMenuItem setTheme = new JMenuItem("Change Theme");
        setTheme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String theme = JOptionPane.showInputDialog(null, "1. System Look\n2. Nimbus\n3. Metal\n4. Motif");
                if(theme == null){return;}

                try {
                    switch (theme) {
                        case "1":
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            break;
                        case "2":
                            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                            break;
                        case "3":
                            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                            break;
                        case "4":
                            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                            break;
                        default:
                            return;
                    }
                    SwingUtilities.updateComponentTreeUI(frame);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                }
            }
        });

        view.add(hide);
        view.add(lineWrap);
        view.addSeparator();
        view.add(setTheme);

        server.add(makeServer);
        server.add(aboutServer);

        file.add(connect);
        file.add(disconnect);
        file.addSeparator();
        file.add(save);
        file.add(delete);
        file.addSeparator();
        file.add(exit);
    }

    /*
    * The main chatting part
    * */
    public static void startRunning(String host, int p, String Uname, String password){
        serverIP = host;
        port = p;
        clientUname = Uname;
        serverPas = password;
        frame.setTitle(clientUname + " - " +serverIP + ":" + port);
        try{
            connectToServer();
            setupStreams();
            whileChatting();
        }catch (EOFException e){
            showMessage("\n Connection Ended");
        }catch (IOException e){
            showMessage("\n FATAL ERROR: " + e.getMessage());
            disconnect.setEnabled(false);
        }finally {
            closeAll();
        }
    }

    private static void connectToServer()throws IOException{
        showMessage("\nAttempting to connect...\n");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        if(!new File("SERVER_TRUST.jts").exists()){
            Utils.extractInnerFiles("SERVER_TRUST.jts");
        }
        System.setProperty("javax.net.ssl.trustStore", "_YOUR_JTS_FILE");
        System.setProperty("javax.net.ssl.trustStorePassword", "_JTS_PASSWORD");

        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        connection = (SSLSocket) sslSocketFactory.createSocket(InetAddress.getByName(serverIP), port);
        final String[] enabledCipherSuites = { "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256" };
        connection.setEnabledCipherSuites(enabledCipherSuites);
        isConnected = true;
        connect.setEnabled(false);
        btnConnect.setEnabled(false);
        disconnect.setEnabled(true);
        showMessage("\nConnected to " + connection.getInetAddress().getHostName());
    }

    private static void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());
    }

    private static void whileChatting() throws IOException{
        ableToType(true);
        sendMessage("\\u\\");
        sendMessage("\\p\\" + serverPas);
        do{
            try{
                message = (String) input.readObject();
                System.out.println(message);
                showMessage("\n"+message);
                if(!frame.isActive()){
                    java.awt.Toolkit.getDefaultToolkit().beep();
                }
            }catch (ClassNotFoundException e){
                showMessage("\n ERROR: " + e.getMessage());
                System.err.println("ERROR: " + e.getMessage());
            }
        }while (!message.equals("SERVER --> END"));
    }

    private static void closeAll(){
        try {
            showMessage("\n Closing down...");
            ableToType(false);

            if(output != null)
                output.close();
            if(input != null)
                input.close();
            if(connection != null)
                connection.close();
            connect.setEnabled(true);
            btnConnect.setEnabled(true);
            isConnected = false;
        }catch (IOException e){
            showMessage("\n ERROR: " + e.getMessage());
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void sendMessage(String message){
        try{
            DateFormat dateFormat = new SimpleDateFormat("MM/dd hh:mm a");
            Date date = new Date();
            output.writeObject(clientUname + "(" + dateFormat.format(date) + ") --> " + message);
            output.flush();

            showMessage("\n" + clientUname + "(" + dateFormat.format(date) + ") --> " + message);
        }catch (IOException e){
            showMessage("ERROR: " + e.getMessage());
        }
    }

    private static void showMessage(final String m){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(m.contains("\\u\\") || m.contains("\\p\\")) {
                  return;
                }

                if(!frame.isActive() && connection.isConnected()){
                    try {
                        if(message.contains("--> ") || message.contains("left the server")) {
                            String[] sender = m.split("\\(");
                            String message = m.substring(m.lastIndexOf(">") + 1);
                            Notification.showNotification(sender[0], message);
                        }
                    }catch (AWTException e){
                        e.printStackTrace();
                    }
                }
                chatWindow.append(m);
                //chatWindow.setText(chatWindow.getText() + m);
                chatWindow.setCaretPosition(chatWindow.getText().length());
            }
        });
    }

    /** End of chatting part */

    private void actionConnect(){
        ConnectToServer connectToServer = new ConnectToServer();
        ConnectToServer.txtUsername.setText(usernames.get(serverList.getSelectedIndex()));
        ConnectToServer.txtHost.setText(hosts.get(serverList.getSelectedIndex()));
        ConnectToServer.txtPort.setText(Integer.toString(ports.get(serverList.getSelectedIndex())));
        ConnectToServer.setPasswordFocused();
    }

    private static void deleteServer(){
        int result = JOptionPane.showConfirmDialog(null, "Delete '" + listModel.get(serverList.getSelectedIndex()) + "'", "Delete", JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.NO_OPTION){return;}
        hosts.remove(serverList.getSelectedIndex());
        usernames.remove(serverList.getSelectedIndex());
        ports.remove(serverList.getSelectedIndex());

        listModel.remove(serverList.getSelectedIndex());

        new Thread(new Runnable() {
            @Override
            public void run() {
                saveServers.removeConf();
                for(int i = 0; i < usernames.size(); i++){
                    saveServers.addServer(usernames.get(i), hosts.get(i), ports.get(i));
                }
                saveServers.saveConfig("Chatter_Save.dat");
            }
        }).start();
    }

    private static void ableToType(final boolean tof){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userText.setEditable(tof);
            }
        });
    }

    public static void clearChatWindow(){
        chatWindow.setText("");
    }
}
