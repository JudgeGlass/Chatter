
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Security;
import java.util.ArrayList;


public class Server {
    private int port;

    private SSLServerSocket server;
    private SSLSocket client;
    public static ArrayList<SSLSocket> clientList = new ArrayList<>();
    public static ArrayList<ObjectInputStream> clientInput = new ArrayList<>();
    public static ArrayList<ObjectOutputStream> clientOutput = new ArrayList<>();

    public static ArrayList<String> clientUsernames = new ArrayList<>();

    public static String message = "";
    public static String history = "";

    private ClientConnected clientConnected;

    public Thread getT() {
        return t;
    }

    static String password = "";

    private boolean isRunning = true;
    private Thread t;

    public Server(int port, String password){
        this.port = port;
        this.password = password;
    }

    public void start(){
        System.out.println("Starting server on port " + port + "...");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        System.setProperty("javax.net.ssl.keyStore", "_YOUR_JKS_FILE");
        System.setProperty("javax.net.ssl.keyStorePassword", "_JKS_PASSWORD");
        try {

            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            server = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
            final String[] enabled = {"TLS_DHE_RSA_WITH_AES_128_CBC_SHA256"};
            server.setEnabledCipherSuites(enabled);
            while (isRunning) {
                System.out.println("Listening...");
                client = (SSLSocket) server.accept();


                if(client.getSession().getCipherSuite().equals("SSL_NULL_WITH_NULL_NULL")){
                    client.close();
                    continue;
                }

                System.out.println("Client connected from " + client.getInetAddress().getHostName()
                                    + " using " + client.getSession().getCipherSuite());

                clientList.add(client);
                t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientConnected clientConnected = new ClientConnected();
                        clientConnected.connected(client);
                    }
                });
                t.start();
            }
        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
