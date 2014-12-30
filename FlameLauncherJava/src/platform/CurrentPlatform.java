package platform;

/**
 * Created by Alec on 12/29/14.
 */
public class CurrentPlatform {
    public enum PlatformType {
        WINDOWS,
        OSX,
        LINUX
    }

    public enum ArchitectureType {
        X86,
        X64
    }

    public static PlatformType getType() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("mac")) {
            return PlatformType.OSX;
        } else if (osName.contains("nix")) {
            return PlatformType.LINUX;
        } else if (osName.contains("win")) {
            return PlatformType.WINDOWS;
        } else {
            System.out.println("Unsupported OS type: " + osName);
            return null;
        }
    }

    public static ArchitectureType getArchitectureType() {
        // TODO: Implement
        return null;
    }
}
