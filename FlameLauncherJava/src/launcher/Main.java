package launcher;

import Json.FlameConfig;
import Json.MinecraftSession;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Main {

    public static void main(String[] args) {
        System.out.println("Scanning for missing files...");
        HttpTaskRunner runner = new HttpTaskRunner(ResourceParser.getResourceTasks());
        runner.run();

        LaunchCommandBuilder builder = new LaunchCommandBuilder();
        Process proc = null;
        
        try {
            String fullCommand = builder.getFullCommand();
            proc = Runtime.getRuntime().exec(fullCommand, null, new File(builder.getRootPath()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (proc != null) {
            System.out.println("Launching Minecraft...");
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                proc.waitFor();
                in.close();
                proc.destroy();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to start child process.");
        }
    }

}
