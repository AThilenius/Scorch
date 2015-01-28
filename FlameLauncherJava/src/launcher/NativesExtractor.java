package launcher;

import utilities.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Alec on 1/28/15.
 */
public class NativesExtractor {
    public void extractAll(List<String> nativesPaths, String nativesFolder) {

        if (new File(nativesFolder).exists()) {
            ZipUtils.deleteDir(new File(nativesFolder));
        }

        System.out.println("Extracting natives to " + nativesFolder);
        for (String path : nativesPaths) {
            try {
                ZipUtils.unzip(path, nativesFolder);
                System.out.println(" - " + path);
            } catch (IOException e) {
                System.out.println("Failed to extract native: " + nativesPaths + " To: " + nativesFolder);
                e.printStackTrace();
            }
        }
    }
}
