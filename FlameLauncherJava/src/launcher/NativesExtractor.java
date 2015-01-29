package launcher;

import utilities.ZipUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Alec on 1/28/15.
 */
public class NativesExtractor {

    private JProgressBar m_progressBar;

    public NativesExtractor(JProgressBar progressBar) {
        m_progressBar = progressBar;
    }

    public void extractAll(List<String> nativesPaths, String nativesFolder) {

        if (new File(nativesFolder).exists()) {
            ZipUtils.deleteDir(new File(nativesFolder));
        }

        System.out.println("Extracting natives to " + nativesFolder);
        for (int i = 0; i < nativesPaths.size(); i++) {
            String path = nativesPaths.get(i);
            try {
                ZipUtils.unzip(path, nativesFolder);
                System.out.println(" - " + path);
            } catch (IOException e) {
                System.out.println("Failed to extract native: " + nativesPaths + " To: " + nativesFolder);
                e.printStackTrace();
            }
            if (m_progressBar != null) {
                m_progressBar.setValue((int) ((float) i / (float) nativesPaths.size() * 100.0f));
            }
        }
    }
}
