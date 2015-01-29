package launcher;

import gui.MainForm;
import platform.CurrentPlatform;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        LaunchCommandBuilder builder = new LaunchCommandBuilder();

        System.out.println("Scanning for missing files...");
        HttpTaskRunner runner = new HttpTaskRunner(ResourceParser.getResourceTasks(), null);
        runner.run();

        // Extract Natives
        NativesExtractor nativesExtractor = new NativesExtractor(null);
        nativesExtractor.extractAll(ResourceParser.getNativesPaths(), builder.getNativesPath());

        Process proc = null;

        try {
            String fullCommand = builder.getFullCommand();
            System.out.println(fullCommand);
            switch(CurrentPlatform.getType()) {
                case OSX:
                    proc = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", fullCommand},
                            null, new File(builder.getRootPath()));
                    break;
                case WINDOWS:
                    proc = Runtime.getRuntime().exec(fullCommand, null, new File(builder.getRootPath()));
                    break;
                case LINUX:
                    proc = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", fullCommand},
                            null, new File(builder.getRootPath()));
                    break;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (proc != null) {
            System.out.println("Launching Minecraft...");
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                while ((line = error.readLine()) != null) {
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
