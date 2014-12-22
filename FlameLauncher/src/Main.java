import Json.MinecraftSession;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static ObjectMapper mapper = new ObjectMapper();

    public static String postJson(URL url, String json) {
        try {
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            writer.write(json);
            writer.flush();

            String returnJson = "";
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                returnJson = returnJson + line;
            }
            writer.close();
            reader.close();

            return returnJson;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {

        String javaPath = System.getProperty("java.home") + "/bin/java";
        String vmArgs = "-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M";
        String minecraftPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "minecraft";
        MinecraftSession userArgs = null;

        try {
            String userArgsJson = postJson(new URL("http://localhost:3000/service/get_minecraft_session"),
                    "{ \"Some Field\": \"Some Value\" }");
            userArgs = mapper.readValue(userArgsJson, MinecraftSession.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Escape spaces in paths
        javaPath.replaceAll(" ", "\\ ");
        minecraftPath.replaceAll(" ", "\\ ");

        String command = "${java_path} -Xdock:icon=${minecraft_path}/Flame-512.png -Xdock:name=Flame -Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M -Djava.library.path=${minecraft_path}/versions/1.7.10-Forge10.13.2.1230/1.7.10-Forge10.13.2.1230-natives -cp ${minecraft_path}/libraries/net/minecraftforge/forge/1.7.10-10.13.2.1230/forge-1.7.10-10.13.2.1230.jar:${minecraft_path}/libraries/net/minecraft/launchwrapper/1.11/launchwrapper-1.11.jar:${minecraft_path}/libraries/org/ow2/asm/asm-all/5.0.3/asm-all-5.0.3.jar:${minecraft_path}/libraries/com/typesafe/akka/akka-actor_2.11/2.3.3/akka-actor_2.11-2.3.3.jar:${minecraft_path}/libraries/com/typesafe/config/1.2.1/config-1.2.1.jar:${minecraft_path}/libraries/org/scala-lang/scala-actors-migration_2.11/1.1.0/scala-actors-migration_2.11-1.1.0.jar:${minecraft_path}/libraries/org/scala-lang/scala-compiler/2.11.1/scala-compiler-2.11.1.jar:${minecraft_path}/libraries/org/scala-lang/plugins/scala-continuations-library_2.11/1.0.2/scala-continuations-library_2.11-1.0.2.jar:${minecraft_path}/libraries/org/scala-lang/plugins/scala-continuations-plugin_2.11.1/1.0.2/scala-continuations-plugin_2.11.1-1.0.2.jar:${minecraft_path}/libraries/org/scala-lang/scala-library/2.11.1/scala-library-2.11.1.jar:${minecraft_path}/libraries/org/scala-lang/scala-parser-combinators_2.11/1.0.1/scala-parser-combinators_2.11-1.0.1.jar:${minecraft_path}/libraries/org/scala-lang/scala-reflect/2.11.1/scala-reflect-2.11.1.jar:${minecraft_path}/libraries/org/scala-lang/scala-swing_2.11/1.0.1/scala-swing_2.11-1.0.1.jar:${minecraft_path}/libraries/org/scala-lang/scala-xml_2.11/1.0.2/scala-xml_2.11-1.0.2.jar:${minecraft_path}/libraries/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar:${minecraft_path}/libraries/lzma/lzma/0.0.1/lzma-0.0.1.jar:${minecraft_path}/libraries/com/mojang/realms/1.3.5/realms-1.3.5.jar:${minecraft_path}/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:${minecraft_path}/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:${minecraft_path}/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:${minecraft_path}/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:${minecraft_path}/libraries/java3d/vecmath/1.3.1/vecmath-1.3.1.jar:${minecraft_path}/libraries/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar:${minecraft_path}/libraries/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar:${minecraft_path}/libraries/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar:${minecraft_path}/libraries/com/paulscode/codecwav/20101023/codecwav-20101023.jar:${minecraft_path}/libraries/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar:${minecraft_path}/libraries/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar:${minecraft_path}/libraries/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar:${minecraft_path}/libraries/io/netty/netty-all/4.0.10.Final/netty-all-4.0.10.Final.jar:${minecraft_path}/libraries/com/google/guava/guava/16.0/guava-16.0.jar:${minecraft_path}/libraries/org/apache/commons/commons-lang3/3.2.1/commons-lang3-3.2.1.jar:${minecraft_path}/libraries/commons-io/commons-io/2.4/commons-io-2.4.jar:${minecraft_path}/libraries/commons-codec/commons-codec/1.9/commons-codec-1.9.jar:${minecraft_path}/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:${minecraft_path}/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:${minecraft_path}/libraries/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar:${minecraft_path}/libraries/com/mojang/authlib/1.5.16/authlib-1.5.16.jar:${minecraft_path}/libraries/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar:${minecraft_path}/libraries/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar:${minecraft_path}/libraries/org/lwjgl/lwjgl/lwjgl/2.9.1/lwjgl-2.9.1.jar:${minecraft_path}/libraries/org/lwjgl/lwjgl/lwjgl_util/2.9.1/lwjgl_util-2.9.1.jar:${minecraft_path}/libraries/tv/twitch/twitch/5.16/twitch-5.16.jar:${minecraft_path}/versions/1.7.10-Forge10.13.2.1230/1.7.10-Forge10.13.2.1230.jar net.minecraft.launchwrapper.Launch --gameDir ${minecraft_path} --assetsDir ${minecraft_path}/assets --version 1.7.10-Forge10.13.2.1230 --assetIndex 1.7.10 --tweakClass cpw.mods.fml.common.launcher.FMLTweaker ${user_args}";
        Process proc = null;

        System.out.println("PWD Path: " + Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        try {
            proc = Runtime.getRuntime().exec("/bin/bash", null, new File("/bin"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (proc != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);

            // Set Vars
            out.println("java_path=" + javaPath);
            out.println("vm_args=" + vmArgs);
            out.println("minecraft_path=" + minecraftPath);
            out.println("user_args=\"" + userArgs.user_args + "\"");
            out.println("echo \"" + command +"\"");
            out.println("echo \"${user_args}\"");

            out.println("cd ${minecraft_path}");
            out.println(command);
            out.println("exit");

            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                proc.waitFor();
                in.close();
                out.close();
                proc.destroy();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
