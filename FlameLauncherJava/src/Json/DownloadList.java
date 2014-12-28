package Json;

import launcher.HttpFileDownloadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec on 12/27/14.
 */
public class DownloadList {
    public List<RemoteResource> linux;
    public List<RemoteResource> windows;
    public List<RemoteResource> osx;
    public List<RemoteResource> common;

    public List<HttpFileDownloadTask> getDownloadTasks() {
        List<HttpFileDownloadTask> tasks = new ArrayList<HttpFileDownloadTask>();

        for (RemoteResource resource : getNatives()) {
            if (!new File(resource.file).exists()) {
                tasks.add(new HttpFileDownloadTask(resource.url, resource.file));
            }
        }

        for (RemoteResource resource : common) {
            if (!new File(resource.file).exists()) {
                tasks.add(new HttpFileDownloadTask(resource.url, resource.file));
            }
        }

        return tasks;
    }

    private List<RemoteResource> getNatives() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("mac") && osx != null && !osx.isEmpty()) {
            return osx;
        } else if (osName.contains("nix") && linux != null && !linux.isEmpty()) {
            return linux;
        } else if (osName.contains("win") && windows != null && !windows.isEmpty()) {
            return windows;
        } else {
            System.out.println("Unsupported OS type: " + osName);
            return null;
        }
    }
}
