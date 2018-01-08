import javax.swing.*;
import java.io.*;

public class Utils {
    public static void writeFile(String fileName, String txt) {
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.print(txt);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            //JOptionPane.showMessageDialog(null, "Error:\n" + e, "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Could not write file.");
            e.printStackTrace();
        }
    }
}
