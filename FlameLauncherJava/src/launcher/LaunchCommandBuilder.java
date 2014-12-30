package launcher;

import Json.FlameConfig;
import Json.MinecraftSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import http.HttpJsonRequest;
import platform.CurrentPlatform;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Alec on 12/29/14.
 */
public class LaunchCommandBuilder {

    private static final String ROOT_PATH =  new File("").getAbsolutePath();
    private static final String MINECRAFT_VERSION = "1.7.10-Forge10.13.2.1230";
    private static final String ASSET_INDEX = "1.7.10";
    private static final String JAVA_PATH = System.getProperty("java.home") + "/bin/java";
    private static final String MINECRAFT_JAR_PATH = ROOT_PATH + "/versions/" + MINECRAFT_VERSION + "/" +
            MINECRAFT_VERSION +".jar";
    private static final String MINECRAFT_LAUNCH_FILE = "net.minecraft.launchwrapper.Launch";
    private static final String TWEAK_CLASS = "cpw.mods.fml.common.launcher.FMLTweaker";

    // Args
    private static final String OSX_ARGS = "-Xdock:icon=" + ROOT_PATH + "/Flame-512.png -Xdock:name=Flame " +
            "-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M";
    private static final String WIN_ARGS = "-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode " +
            "-XX:-UseAdaptiveSizePolicy -Xmn128M";
    private static final String LINUX_ARGS = "-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode " +
            "-XX:-UseAdaptiveSizePolicy -Xmn128M";

    // Globing
    private static final String[] GLOB_EXCLUDE = new String[] {
            "natives" };

    // Commands
    private static final String OSX_COMMAND = "${java_path} ${platform_args} -Djava.library.path=${natives_path} -cp " +
            "${library_glob}:${minecraft_jar_path} ${minecraft_launch_file} --gameDir ${root_path} --assetsDir " +
            "${root_path}/assets --version ${minecraft_version} --assetIndex ${asset_index} --tweakClass " +
            "${tweak_class} ${session_args}";
    private static final String WINDOWS_COMMAND = "${java_path} ${platform_args} -Djava.library.path=${natives_path} -cp " +
            "${library_glob}:${minecraft_jar_path} ${minecraft_launch_file} --gameDir ${root_path} --assetsDir " +
            "${root_path}/assets --version ${minecraft_version} --assetIndex ${asset_index} --tweakClass " +
            "${tweak_class} ${session_args}";
    private static final String LINUX_COMMAND = "${java_path} ${platform_args} -Djava.library.path=${natives_path} -cp " +
            "${library_glob}:${minecraft_jar_path} ${minecraft_launch_file} --gameDir ${root_path} --assetsDir " +
            "${root_path}/assets --version ${minecraft_version} --assetIndex ${asset_index} --tweakClass " +
            "${tweak_class} ${session_args}";

    private static ObjectMapper m_jsonMapper = new ObjectMapper();


    public String getFullCommand() {
        String command = "";
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

        // Replace ${} strings
        return command.replace("${java_path}",              JAVA_PATH)
                .replace("${platform_args}",          getPlatformArgs())
                .replace("${natives_path}",           getNativesPath())
                .replace("${library_glob}",           globLibFolder())
                .replace("${minecraft_jar_path}",     MINECRAFT_JAR_PATH)
                .replace("${minecraft_launch_file}",  MINECRAFT_LAUNCH_FILE)
                .replace("${root_path}",              ROOT_PATH)
                .replace("${minecraft_version}",      MINECRAFT_VERSION)
                .replace("${asset_index}",            ASSET_INDEX)
                .replace("${tweak_class}",            TWEAK_CLASS)
                .replace("${session_args}",           getSessionArgs());
    }

    public String getRootPath() {
        return ROOT_PATH;
    }

    private void checkForMissingFiles() {
        System.out.println("Synchronizing game files");
        HttpTaskRunner runner = new HttpTaskRunner(ResourceParser.getResourceTasks());
        runner.run();
    }

    private String getPlatformArgs() {
        switch(CurrentPlatform.getType()) {
            case OSX: return OSX_ARGS;
            case WINDOWS: return WIN_ARGS;
            case LINUX: return LINUX_ARGS;
            default: return null;
        }
    }

    private String getSessionArgs() {
//        try {
//            System.out.print("Authenticating with Blaze... ");
//            FlameConfig config = FlameConfig.getConfig();
//            HttpJsonRequest request = new HttpJsonRequest(new URL(config.session_service_url));
//            String userArgsJson = request.postRequest("{ \"user_guid\": \"" + config.user_guid + "\" }");
//            MinecraftSession sessionInfo = m_jsonMapper.readValue(userArgsJson, MinecraftSession.class);
//            if (sessionInfo.error != null && !sessionInfo.error.isEmpty()) {
//                System.out.println("Failed to login to Blaze. Given reason: " + sessionInfo.error);
//                return null;
//            }
//            System.out.println("Done.");
//            return sessionInfo.user_args;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;

        // HACK: Offline
        return "--username deathsshado0 --uuid a84c72ce2e9445e4b220914678f2cb6d --accessToken b3b9d960ed794a9188518163a30bb043 --userProperties {} --userType legacy";
    }

    private String globLibFolder() {
        String globAll = globFiles(new File("libraries/").listFiles(), ":", GLOB_EXCLUDE);
        globAll = globAll.substring(0, globAll.length() - 1);

        // TODO: Figure out what to do with this shit...
        //globAll.replace(" ", "\\ ");
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
                    glob += file.getAbsolutePath() + ":";
                }
            }
        }

        return glob;
    }

    private String getNativesPath() {
        // TODO: OS Dependant
        return ROOT_PATH + "/versions/" + MINECRAFT_VERSION + "/" + MINECRAFT_VERSION + "-natives";
    }

}