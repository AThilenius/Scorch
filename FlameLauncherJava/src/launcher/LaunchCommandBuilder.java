package launcher;

import Json.FlameConfig;
import Json.MinecraftSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import http.HttpJsonRequest;
import platform.CurrentPlatform;
import platform.GlobalExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Alec on 12/29/14.
 */
public class LaunchCommandBuilder {

    private static final String ROOT_PATH =  new File("").getAbsolutePath();
    private static final String MINECRAFT_VERSION = "1.7.10-Forge10.13.2.1230";
    private static final String ASSET_INDEX = "1.7.10";
    //private static final String JAVA_PATH = "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/bin/java";
    private static final String MINECRAFT_JAR_PATH = ROOT_PATH + "/versions/" + MINECRAFT_VERSION + "/" +
            MINECRAFT_VERSION +".jar";
    private static final String NATIVES_PATH = ROOT_PATH + "/versions/" + MINECRAFT_VERSION + "/" +
            MINECRAFT_VERSION + "-natives";
    private static final String MINECRAFT_LAUNCH_FILE = "net.minecraft.launchwrapper.Launch";
    private static final String TWEAK_CLASS = "cpw.mods.fml.common.launcher.FMLTweaker";

    // Globing
    private static final String[] GLOB_EXCLUDE = new String[] { "natives" };

    // Commands
    private static final String[] OSX_COMMAND = new String[] {
            "${java_path}",

            // OSX Platform Args
            "-Xdock:icon=" + ROOT_PATH + "assets/flame/Flame-512.png",
            "-Xdock:name=Flame",
            "-Xmx1G",
            "-XX:+UseConcMarkSweepGC",
            "-XX:+CMSIncrementalMode",
            "-XX:-UseAdaptiveSizePolicy",
            "-Xmn128M",

            "-Djava.library.path=${natives_path}",
            "-cp",
            "${library_glob}${minecraft_jar_path}",
            "${minecraft_launch_file}",
            "--gameDir", "${root_path}",
            "--assetsDir", "${root_path}/assets",
            "--version", "${minecraft_version}",
            "--assetIndex", "${asset_index}",
            "--tweakClass", "${tweak_class}"};
    private static final String[] WINDOWS_COMMAND = new String[] {
            "${java_path}",

            // Windows Platform Args
            "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump",
            "-Xmx1G",
            "-XX:+UseConcMarkSweepGC",
            "-XX:+CMSIncrementalMode",
            "-XX:-UseAdaptiveSizePolicy",
            "-Xmn128M",

            "-Djava.library.path=${natives_path}",
            "-cp",
            "${library_glob}${minecraft_jar_path}",
            "${minecraft_launch_file}",
            "--gameDir", "${root_path}",
            "--assetsDir", "${root_path}/assets",
            "--version", "${minecraft_version}",
            "--assetIndex", "${asset_index}",
            "--tweakClass", "${tweak_class}"};
    private static final String[] LINUX_COMMAND = new String[] {
            "${java_path}",

            // Linux Platform Args
            "-Xmx1G",
            "-XX:+UseConcMarkSweepGC",
            "-XX:+CMSIncrementalMode",
            "-XX:-UseAdaptiveSizePolicy",
            "-Xmn128M",

            "-Djava.library.path=${natives_path}",
            "-cp",
            "${library_glob}${minecraft_jar_path}",
            "${minecraft_launch_file}",
            "--gameDir", "${root_path}",
            "--assetsDir", "${root_path}/assets",
            "--version", "${minecraft_version}",
            "--assetIndex", "${asset_index}",
            "--tweakClass", "${tweak_class}"};

    private static ObjectMapper m_jsonMapper = new ObjectMapper();


    public String[] getFullCommand(String javaPath) {
        String[] command = null;
        switch(CurrentPlatform.getType()) {
            case OSX:
                command = OSX_COMMAND;
                break;
            case WINDOWS:
                command = WINDOWS_COMMAND;
                break;
            case LINUX:
                command = LINUX_COMMAND;
                break;
        }

        String[] sessionArgs = getSessionArgs();
        String[] fullCommand = new String[command.length + sessionArgs.length];

        // Replace ${} strings in command
        for (int i = 0; i < command.length; i++) {
            fullCommand[i] = command[i].replace("${java_path}", javaPath)
                    .replace("${natives_path}", getNativesPath())
                    .replace("${library_glob}", globLibFolder())
                    .replace("${minecraft_jar_path}", MINECRAFT_JAR_PATH)
                    .replace("${minecraft_launch_file}", MINECRAFT_LAUNCH_FILE)
                    .replace("${root_path}", ROOT_PATH)
                    .replace("${minecraft_version}", MINECRAFT_VERSION)
                    .replace("${asset_index}", ASSET_INDEX)
                    .replace("${tweak_class}", TWEAK_CLASS);
        }

        // Add in session args
        for (int i = 0; i < sessionArgs.length; i++) {
            fullCommand[i + command.length] = sessionArgs[i];
        }

        return fullCommand;
    }

    public String getRootPath() {
        return ROOT_PATH;
    }

    public String getJavaPath() { return System.getProperty("java.home") + "/bin"; }

    public String getNativesPath() {
        return NATIVES_PATH;
    }

    private String[] getSessionArgs() {
        try {
            System.out.print("Authenticating with Blaze... ");
            FlameConfig config = FlameConfig.getConfig();
            HttpJsonRequest request = new HttpJsonRequest(new URL(config.session_service_url));
            String userArgsJson = request.postRequest("{ \"user_guid\": \"" + config.user_guid + "\" }");
            MinecraftSession sessionInfo = m_jsonMapper.readValue(userArgsJson, MinecraftSession.class);
            if (sessionInfo.error != null && !sessionInfo.error.isEmpty()) {
                System.out.println("Failed to login to Blaze. Given reason: " + sessionInfo.error);
                return null;
            }
            System.out.println("Done.");
            return new String[] {
                    "--username", sessionInfo.username,
                    "--uuid", sessionInfo.uuid,
                    "--accessToken", sessionInfo.access_token,
                    "--userProperties", sessionInfo.user_properties,
                    "--userType", sessionInfo.user_type };
        } catch (IOException e) {
            e.printStackTrace();
            GlobalExceptionHandler.errorOut(e);
        }

        return null;
    }

    private String globLibFolder() {
        String globAll = "";
        switch(CurrentPlatform.getType()) {
            case OSX:
                globAll = globFiles(new File("libraries/").listFiles(), ":", GLOB_EXCLUDE);
                break;
            case WINDOWS:
                globAll = globFiles(new File("libraries/").listFiles(), ";", GLOB_EXCLUDE);
                break;
            case LINUX:
                globAll = globFiles(new File("libraries/").listFiles(), ":", GLOB_EXCLUDE);
                break;
        }

        return globAll;
    }

    private String globFiles(File[] files, String separator, String[] exclusions) {
        String glob = "";
        for (File file : files) {
            if (file.isDirectory()) {
                glob += globFiles(file.listFiles(), separator, exclusions);
            } else {
                boolean isExcluded = false;
                for (String exclude : exclusions) {
                    if (file.getAbsolutePath().contains(exclude)) {
                        isExcluded = true;
                    }
                }
                if (!isExcluded) {
                    glob += file.getAbsolutePath() + separator;
                }
            }
        }

        return glob;
    }

}
