package launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardCopyOption;
import java.security.Security;

/**
 * Created by Alec on 12/27/14.
 */
public class HttpFileDownloadTask implements Runnable {

    private static boolean s_wasInitialized;

    private String m_url;
    private String m_file;

    public HttpFileDownloadTask(String url, String file) {
        m_url = url;
        m_file = file;
    }

    @Override
    public void run() {
        Initialize();

        URL website = null;
        try {
            website = new URL(m_url);

            File outFile = new File(m_file);
            if(!outFile.exists()) {
                outFile.getParentFile().mkdirs();
                outFile.createNewFile();
            }

            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(outFile, false);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (MalformedURLException e) {
            // e.printStackTrace();
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    private static final void Initialize() {
        if (s_wasInitialized) {
            return;
        }
        s_wasInitialized = true;

        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    }
}
