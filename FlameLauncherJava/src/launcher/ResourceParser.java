package launcher;

import Json.DownloadList;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            encoded = Files.readAllBytes(Paths.get("DownloadList.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonText = new String(encoded, Charset.defaultCharset());

        // Parse JSON
        DownloadList downloadList = null;
        try {
            downloadList = mapper.readValue(jsonText, DownloadList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return downloadList.getDownloadTasks();
    }
}
