package com.zozok123.servergen;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ServerGen {


    public static void main(String[] args) {

        SpigotVersion version = SpigotVersion.LATEST;

        boolean nextArgVer = true;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equalsIgnoreCase("--rev") && i != args.length - 1) {
                nextArgVer = true;
            }

            if (nextArgVer) {
                version = SpigotVersion.parse(arg);
            }
        }

        try {
            System.out.println("Downloading Spigot " + version.getUserFriendlyName());
            download(version.getVersionName());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            System.out.println("Adding startup files");
            addStartupFiles();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            System.out.println("Adding eula file");
            addEulaFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Done");
    }

    private static void addEulaFile() throws IOException {
        File eula = new File("eula.txt");

        if (!eula.exists()) {
            eula.createNewFile();
        }

        String eulaText = "eula=true";

        BufferedWriter writer = new BufferedWriter(new FileWriter(eula));
        writer.write(eulaText);
        writer.close();
    }

    private static void addStartupFiles() throws IOException {
        File startBat = new File("start.bat");

        if (!startBat.exists()) {
            startBat.createNewFile();
        }

        String startBatText =
                "java -jar spigot.jar\r\n" +
                        "pause";

        BufferedWriter writer = new BufferedWriter(new FileWriter(startBat));
        writer.write(startBatText);
        writer.close();

        /* ---- */

        File startSh = new File("start.sh");

        if (!startSh.exists()) {
            startSh.createNewFile();
        }

        String startShText =
                "java -jar spigot.jar\n" +
                        "read -n 1 -s";

        writer = new BufferedWriter(new FileWriter(startSh));
        writer.write(startShText);
        writer.close();
    }

    private static void download(String version) throws IOException {
        URL website = new URL("http://tcpr.ca/files/spigot/spigot" + "-" + version + ".jar");
        URLConnection connection = website.openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        try (InputStream in = connection.getInputStream()) {
            Files.copy(in, new File("spigot.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public enum SpigotVersion {
        LATEST("latest", "latest"),

        V1_10("1.10-R0.1-SNAPSHOT-latest", "1.10"),

        V_1_9_4("1.9.4-R0.1-SNAPSHOT-latest", "1.9.4"),
        V_1_9_2("1.9.2-R0.1-SNAPSHOT-latest", "1.9.2"),
        V_1_9("1.9-R0.1-SNAPSHOT-latest", "1.9"),

        V_1_8_8("1.8.8-R0.1-SNAPSHOT-latest", "1.8.8"),
        V_1_8_7("1.8.7-R0.1-SNAPSHOT-latest", "1.8.7"),
        v_1_8_6("1.8.6-R0.1-SNAPSHOT-latest", "1.8.6"),
        V_1_8_4("1.8.4-R0.1-SNAPSHOT-latest", "1.8.5"),
        V_1_8_3("1.8.3-R0.1-SNAPSHOT-latest", "1.8.5"),
        V_1_8("1.8-R0.1-SNAPSHOT-latest", "1.8.5"),

        V_1_7_10("1.7.10-SNAPSHOT-b1657", "1.7.10"),
        V_1_7_9("1.7.9-R0.2-SNAPSHOT-1486", "1.7.9"),
        V_1_7_5("1.7.5-R0.1-SNAPSHOT-1387", "1.7.5"),
        V_1_7_2("1.7.2-R0.4-SNAPSHOT-1339", "1.7.2"),

        V_1_6_4("1.6.4-R2.1-SNAPSHOT", "1.6.4"),

        V_1_6_2("1.6.2-R1.1-SNAPSHOT", "1.6.2"),

        V_1_5_2("1.5.2-R1.1-SNAPSHOT", "1.5.2"),
        V_1_5_1("1.5.2-R0.1-SNAPSHOT", "1.5.1"),

        V_1_4_7("1.4.7-R1.1-SNAPSHOT", "1.4.7");

        /* ---- */

        private String versionName;
        private String userFriendlyName;

        SpigotVersion(String versionName, String userFriendlyName) {
            this.versionName = versionName;
            this.userFriendlyName = userFriendlyName;
        }

        public static SpigotVersion parse(String s) {
            for (SpigotVersion spigotVersion : values()) {
                if (spigotVersion.getUserFriendlyName().equalsIgnoreCase(s)) {
                    return spigotVersion;
                }
            }
            return LATEST;
        }

        public String getVersionName() {
            return versionName;
        }

        public String getUserFriendlyName() {
            return userFriendlyName;
        }
    }

}
