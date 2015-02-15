package Json;

import com.fasterxml.jackson.databind.ObjectMapper;
import platform.GlobalExceptionHandler;
import utilities.IOUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Alec on 12/27/14.
 */
public class FlameConfig {
    private static ObjectMapper mapper = new ObjectMapper();

    public String user_guid;
    public String load_balancer_ip;
    public int load_balancer_port;
    public String session_service_url;

    public static FlameConfig getConfig() {
        // Read File
        byte[] encoded = new byte[0];
        try {
            encoded = IOUtil.readFile(new File("FlameConfig.json"));
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }
        String jsonText = new String(encoded, Charset.defaultCharset());

        // Parse JSON
        FlameConfig config = null;
        try {
            config = mapper.readValue(jsonText, FlameConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }

        return config;
    }
}
