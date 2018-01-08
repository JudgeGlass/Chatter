package com.JudgeGlass.Chatter.misc;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.rmi.CORBA.Util;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

public class Utils {
    public static String indexOf(String txt, char ch) {
        return txt.substring(txt.lastIndexOf(ch) + 1);
    }

    public String getHelpFile() {
        String cont = "";
        InputStream input = getClass().getResourceAsStream("/com/JudgeGlass/Chatter/misc/Chatt_Doc.txt");
        try {
            cont = IOUtils.toString(input, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cont;
    }

    public static String readLine(String fileName, int lineNumber) {
        String line;
        try {
            line = Files.readAllLines(Paths.get(fileName)).get(lineNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }
        return line;
    }

    public static void writeFile(String fileName, String txt) {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.print(txt);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null, "Error:\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void extractInnerFiles(String fileName){
        InputStream ddlStream = Utils.class.getClassLoader().getResourceAsStream("com/JudgeGlass/Chatter/misc/external_files/" + fileName);
        try(FileOutputStream fos = new FileOutputStream(fileName);){
            byte[] buf = new byte[2048];
            int r;
            while(-1 != (r = ddlStream.read(buf))) {
                fos.write(buf, 0, r);
            }
        }catch (Exception e){
            System.out.println("Could not extract files.");
            e.printStackTrace();
        }
    }

    public static boolean fileEmpty(String filename){
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            if(br.readLine() == null){
                return true;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public static int getFileLineCount(String filename){
        try{

            File file =new File(filename);

            if(file.exists()){

                FileReader fr = new FileReader(file);
                LineNumberReader lnr = new LineNumberReader(fr);

                int linenumber = 0;

                while (lnr.readLine() != null){
                    linenumber++;
                }



                lnr.close();
                return linenumber;

            }else{
                System.out.println("File does not exists!");
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return 0;

    }
}