import java.io.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        String runCommand = "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home/bin/java -Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M -Djava.library.path=/Users/Alec/Library/Application\\ Support/minecraft/natives -cp /Users/Alec/Library/Application\\ Support/minecraft/libraries/com/mojang/realms/1.3.5/realms-1.3.5.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/java3d/vecmath/1.3.1/vecmath-1.3.1.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/com/paulscode/codecwav/20101023/codecwav-20101023.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/io/netty/netty-all/4.0.10.Final/netty-all-4.0.10.Final.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/com/google/guava/guava/15.0/guava-15.0.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/commons-io/commons-io/2.4/commons-io-2.4.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/commons-codec/commons-codec/1.9/commons-codec-1.9.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/com/mojang/authlib/1.5.16/authlib-1.5.16.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/org/lwjgl/lwjgl/lwjgl/2.9.1/lwjgl-2.9.1.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/org/lwjgl/lwjgl/lwjgl_util/2.9.1/lwjgl_util-2.9.1.jar:/Users/Alec/Library/Application\\ Support/minecraft/libraries/tv/twitch/twitch/5.16/twitch-5.16.jar:/Users/Alec/Library/Application\\ Support/minecraft/versions/1.7.10/1.7.10.jar net.minecraft.client.main.Main --username deathsshado0 --version 1.7.10 --gameDir /Users/Alec/Library/Application\\ Support/minecraft --assetsDir /Users/Alec/Library/Application\\ Support/minecraft/assets --assetIndex 1.7.10 --uuid a84c72ce2e9445e4b220914678f2cb6d --accessToken 6fb0da0f258f4fd9b63473e5fff72119 --userProperties {} --userType legacy";
        String directory = "/Users/Alec/Library/Application\\ Support/minecraft/versions/1.7.10";
//        try {
//            ProcessBuilder pb = new ProcessBuilder(runCommand);
//            pb.inheritIO();
//            pb.directory(new File(directory));
//            pb.start();
//            //Process p = Runtime.getRuntime().exec(new String[]{runCommand}, null, new File("/Users/Alec/Library/Application\\ Support/minecraft/versions/1.7.10"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        File wd = new File("/bin");
        System.out.println(wd);
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("/bin/bash", null, wd);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (proc != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
            out.println(runCommand);
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

        System.out.println("Done.");
    }

//    private static String executeCommand(String command) {
//
//        StringBuffer output = new StringBuffer();
//
//        Process p;
//        try {
//
//            p.waitFor();
//            BufferedReader reader =
//                    new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//            String line = "";
//            while ((line = reader.readLine())!= null) {
//                output.append(line + "\n");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return output.toString();
//
//    }


}
