package com.JudgeGlass.Chatter.Client;


import com.JudgeGlass.Chatter.misc.Utils;

public class SaveServers {
    private String conf = "";

    public void addServer(String username, String host, int port){
        writeToConf("## SERVER ##");
        writeToConf("username=" + username);
        writeToConf("host_name=" + host);
        writeToConf("port=" + Integer.toString(port));
        writeToConf("## SERVER_END ##\n");
    }

    public void saveConfig(String file){
        Utils.writeFile(file, conf);
    }

    private void writeToConf(String text){
        conf += text + "\n";
    }

    public String getConf(){
        return conf;
    }

    public String getUsername(final String fileName){
        return Utils.readLine(fileName, 1);
    }

    public void removeConf(){
        conf = "";
    }
}
