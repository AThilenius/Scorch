package launcher;

import platform.CurrentPlatform;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Alec on 1/29/15.
 */
public class JavaFinder {

    public String[] OSX_SCAN_LOCATIONS = new String[] {
            "/Library/Java/JavaVirtualMachines/",
            "/System/Library/Java/JavaVirtualMachines/" };

    private String m_javaPath = null;
    private boolean m_correctVersion = false;

    public JavaFinder() {
        // Scan File System
        switch (CurrentPlatform.getType()) {
            case OSX:
                // Scan all locations for 1.6.x
                for (String scanLocation : OSX_SCAN_LOCATIONS) {
                    File scanDirectory = new File(scanLocation);

                    File[] files = scanDirectory.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if(files[i].getName().contains("1.6.")) {
                            m_javaPath = files[i].getAbsolutePath() + "/Contents/Home/bin/java";
                            m_correctVersion = true;

                            // Ensure that path exists
                            if (!(new File(m_javaPath)).exists()) {
                                m_javaPath = null;
                                m_correctVersion = false;
                            }

                            break;
                        }
                    }
                }
                break;

            case WINDOWS:
                // Scan all locations for 1.6.x
                String programFilesPath = System.getenv("ProgramFiles");
                String[] programPaths = new String[2];

                if (programFilesPath == null) {
                    // Default to fallback C drive
                    programFilesPath = "C:";
                }

                programPaths[0] = programFilesPath.split(":")[0] + ":/Program Files/Java";
                programPaths[1] = programFilesPath.split(":")[0] + ":/Program Files (x86)/Java";

                for (String scanLocation : programPaths) {
                    File scanDirectory = new File(scanLocation);

                    if (!scanDirectory.exists()) {
                        continue;
                    }

                    File[] files = scanDirectory.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if(files[i].getName().contains("1.6.")) {
                            m_javaPath = files[i].getAbsolutePath() + "/bin/java.exe";
                            m_correctVersion = true;

                            // Ensure that path exists
                            if (!(new File(m_javaPath)).exists()) {
                                m_javaPath = null;
                                m_correctVersion = false;
                            }

                            break;
                        }
                    }
                }
                break;
        }

        // Use the current version if 1.6.x can't be found
        if (m_javaPath == null) {
            m_javaPath = System.getProperty("java.home") + "/bin/java";
            m_correctVersion = false;
        }

    }

    public boolean hasCorrectVersion() {
        return m_correctVersion;
    }

    public String getJavaPath() {
        return m_javaPath;
    }

    public String getDownloadLink() {
        switch (CurrentPlatform.getType()) {
            case OSX:
                return "http://support.apple.com/kb/DL1572";
            case WINDOWS:
                return "http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase6-419409.html#jdk-6u45-oth-JPR";
        }

        return null;
    }
}
