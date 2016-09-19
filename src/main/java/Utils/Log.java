package Utils;

import Model.Paper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by rabbit on 6/10/2016.
 */
public class Log {

    static boolean TO_CONSOLE = true;
    static boolean TO_FILE = true;


    public static void toConsole(Object obj) {
        if (TO_CONSOLE) {
            System.out.println(obj.toString());
        }
    }

    public static void toFile(Object obj, FileWriter logFile) {
        try {
            if (TO_FILE) {
                logFile.write(obj.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
