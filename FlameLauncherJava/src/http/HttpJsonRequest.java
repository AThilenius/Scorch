package http;

import platform.GlobalExceptionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Alec on 12/29/14.
 */
// TODO: Add keep-alive
public class HttpJsonRequest {

    private boolean m_isConnected;
    private URL m_url;
    private URLConnection m_urlConnection;


    public HttpJsonRequest(URL url) {
        m_url = url;
    }

    // Will be called automatically if not done manually
    public boolean connect() {
        try {
            m_urlConnection = m_url.openConnection();
            m_urlConnection.setDoOutput(true);
            m_isConnected = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }

        return false;
    }

    public String postRequest(String json) {
        if (!m_isConnected) {
            if (!connect()) {
                return null;
            }
        }

        try {
            OutputStreamWriter writer = new OutputStreamWriter(m_urlConnection.getOutputStream());

            writer.write(json);
            writer.flush();

            String returnJson = "";
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(m_urlConnection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                returnJson = returnJson + line;
            }
            writer.close();
            reader.close();

            return returnJson;
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }

        return null;
    }

    public boolean getIsConnected() {
        return m_isConnected;
    }

}
