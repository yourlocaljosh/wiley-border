package klism.wileyborder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("wiley_border.json");

    private static WileyBorderConfig CONFIG = new WileyBorderConfig();

    private ConfigManager() {}

    public static WileyBorderConfig get() {
        return CONFIG;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }
        try {
            String json = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
            WileyBorderConfig loaded = GSON.fromJson(json, WileyBorderConfig.class);
            if (loaded != null) CONFIG = loaded;
        } catch (Exception e) {
            CONFIG = new WileyBorderConfig();
            save();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = GSON.toJson(CONFIG);
            Files.writeString(CONFIG_PATH, json, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }
}