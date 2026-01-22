import java.io.*;
import java.net.URL;
import java.nio.file.*;

public class Piston {
    private static final String GEYSER_URL = "https://download.geysermc.org/v2/projects/geyser/versions/latest/builds/latest/downloads/standalone";
    private static Process geyserProcess;

    // Downloads Geyser if it's missing
    public static void checkAndDownloadGeyser() throws IOException {
        Path path = Paths.get("Geyser-Standalone.jar");
        if (!Files.exists(path)) {
            URL url = new URL(GEYSER_URL);
            try (InputStream in = url.openStream()) {
                Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    // Updates config and starts the bridge
    public static void startBridge(String ip, int port) throws IOException {
        String configData = "remote:\n" +
                            "  address: " + ip + "\n" +
                            "  port: " + port + "\n" +
                            "  auth-type: online\n" +
                            "bedrock:\n" +
                            "  address: 0.0.0.0\n" +
                            "  port: 19132\n";

        Files.write(Paths.get("config.yml"), configData.getBytes());

        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "Geyser-Standalone.jar");
        pb.inheritIO(); 
        geyserProcess = pb.start();
    }

    public static void stopBridge() {
        if (geyserProcess != null) {
            geyserProcess.destroy();
        }
    }
}