import javax.net.ssl.SSLSocket;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientConnected {

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private SSLSocket connectedClient;

    private String username = "";

    private Thread checkConnected;

    //private String serverPas = "1234";
    private boolean historyShown = false;
    private boolean isRunning = true;
    private boolean serverAuth = false;
    private boolean gottenUsername = false;
    private String firstMessage = "";

    public void connected(SSLSocket socket){
        //clients.add(socket);
        connectedClient = socket;
        try {
            while (true) {
                try {
                    setStream();
                    checkConnected = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(isRunning){
                                if (connectedClient.isClosed()) {
                                    System.out.println(username + " has left the server");
                                    if(input != null && output != null )
                                        sendMessage("> " + username + " has left the server");
                                    endConnection();
                                    break;
                                }
                            }
                        }
                    });
                    checkConnected.start();

                    chatting();
                } catch (EOFException e) {
                    System.out.println("> " + username + " has left");
                    sendMessage(username + " has left.");
                    endConnection();
                } finally {
                    closeAll();
                    break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setStream() throws IOException{
        output = new ObjectOutputStream(connectedClient.getOutputStream());
        Server.clientOutput.add(output);
        output.flush();
        input = new ObjectInputStream(connectedClient.getInputStream());
        Server.clientInput.add(input);
    }

    public void closeAll() throws IOException{
        if(input != null)
            input.close();
        if(output != null)
            output.close();
        if(connectedClient != null)
            connectedClient.close();
        isRunning = false;
    }

    private void chatting() throws IOException{
        while(isRunning){
            try{
                Server.message = (String) input.readObject();
                String test = Server.message;

                if(test.substring(test.lastIndexOf("> ")+1).equals("exit")){
                    System.out.println("> " + getUsernameFromMessage(test) + " has left");
                    sendMessage(getUsernameFromMessage(test) + " has left.");
                    endConnection();
                    break;
                }else if(test.substring(test.lastIndexOf(" ")+1).equals("\\u\\")){
                    for (int i = 0; i<Server.clientUsernames.size(); i++){
                        if(Server.clientUsernames.get(i).equals(getUsernameFromMessage(test))){
                            sendPrivateMessage("Error: username taken");
                            endConnection();
                            return;
                        }
                    }
                    username = getUsernameFromMessage(test);
                    addUsername(getUsernameFromMessage(test));
                    gottenUsername = true;
                    continue;
                }else if(test.substring(test.lastIndexOf(" ")+1).contains("\\p\\")){
                    String password = test.substring(test.lastIndexOf("\\")+1);
                    if(!password.equals(Server.password)){
                        sendPrivateMessage("Error: password is invalid");
                        endConnection();
                        return;
                    }
                    serverAuth = true;
                    System.out.println("> " + username + " has connectd to the server");
                    sendMessage(username + " has connected to the server");
                    sendPrivateMessage(Server.history);
                    continue;
                }else if(test.substring(test.lastIndexOf(" ")+1).equals("clients")){
                    sendPrivateMessage(getUsernames());
                }

                if(gottenUsername && !serverAuth){
                    endConnection();
                }

                Server.history += test + "\n";
                if(Server.history.length() >= 10000){
                    /*DateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
                    Date date = new Date();
                    if(!new File("Chat_History").exists()){
                        File chatHistory = new File("Chat_History");
                        chatHistory.mkdir();
                    }
                    Utils.writeFile(dateFormat.format(date), Server.history);*/
                    Server.history = "";
                }

                sendMessage(Server.message);
            }catch (ClassNotFoundException e){
                System.out.println("> ERROR: Bad character");
                e.printStackTrace();
                Server.message = "";
            }
        }
    }

    public void sendMessage(String message){
        try {
            for (int i = 0; i < Server.clientList.size(); i++) {
                if(Server.clientOutput.get(i) == output){continue;}
                Server.clientOutput.get(i).writeObject(message);
                //output.writeObject(message);
                Server.clientOutput.get(i).flush();
            }
        }catch (IOException e){
            System.out.println("> Could not send message");
            e.printStackTrace();
        }
    }

    private void sendPrivateMessage(String message) throws IOException{
        output.writeObject(message);
        output.flush();
    }

    private String getUsernameFromMessage(String message){
        String[] parts = message.split("\\(");
        return parts[0];
    }

    private void addUsername(String name){
        Server.clientUsernames.add(name);
    }

    private void removeUsername(){
       for(int i = 0; i<Server.clientUsernames.size(); i++){
           if(Server.clientUsernames.get(i).equals(username)){
               Server.clientUsernames.remove(i);
               break;
           }
       }
    }

    private void endConnection(){
        removeUsername();
        //Server.clientList.remove(Server.clientList.indexOf(connectedClient));
        for(int i = 0; i<Server.clientList.size(); i++){
            if(Server.clientList.get(i) == connectedClient){
                Server.clientList.remove(i);
            }
        }

        for(int i = 0; i<Server.clientOutput.size(); i++){
            if(Server.clientOutput.get(i) == output){
                Server.clientOutput.remove(output);
            }
        }
        //Server.clientOutput.remove(Server.clientOutput.indexOf(output));
        //Server.clientInput.remove(Server.clientInput.indexOf(input));
        for(int i = 0; i<Server.clientInput.size(); i++){
            if(Server.clientInput.get(i) == input){
                Server.clientInput.remove(i);
            }
        }
        try {
            closeAll();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String getUsernames(){
        String clients = "";
        for(int i = 0; i<Server.clientUsernames.size(); i++){
            clients += Server.clientUsernames.get(i) + "\n";
        }

        return clients;
    }
}
