package launcher;

import Json.DownloadList;
import com.fasterxml.jackson.databind.ObjectMapper;
import platform.GlobalExceptionHandler;
import utilities.IOUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Alec on 12/27/14.
 */
public class ResourceParser {
    private static ObjectMapper mapper = new ObjectMapper();

    public static List<HttpFileDownloadTask> getResourceTasks() {
        // Read File
        byte[] encoded = new byte[0];
        try {
            File downloadListFile = new File("DownloadList.json");
            if (downloadListFile == null || !downloadListFile.exists()) {

            }
            encoded = IOUtil.readFile(new File("DownloadList.json"));
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }
        String jsonText = new String(encoded, Charset.defaultCharset());

        // Parse JSON
        DownloadList downloadList = null;
        try {
            downloadList = mapper.readValue(jsonText, DownloadList.class);
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }

        return downloadList.getDownloadTasks();
    }

    public static List<String> getNativesPaths() {
        // Read File
        byte[] encoded = new byte[0];
        try {
            encoded = IOUtil.readFile(new File("DownloadList.json"));
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }
        String jsonText = new String(encoded, Charset.defaultCharset());

        // Parse JSON
        DownloadList downloadList = null;
        try {
            downloadList = mapper.readValue(jsonText, DownloadList.class);
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }

        return downloadList.getNativesPaths();
    }
}
