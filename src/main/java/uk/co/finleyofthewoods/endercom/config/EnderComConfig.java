package uk.co.finleyofthewoods.endercom.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.jspecify.annotations.NonNull;
import uk.co.finleyofthewoods.endercom.chatguard.configuration.ChatGuardConfig;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import static uk.co.finleyofthewoods.endercom.constant.EnderComConstants.log;

/**
 * Root config class for EnderCom, handles loading and saving of the config file.
 *
 * @since 1.0.0
 * @author FinleyOfTheWoods
 */
public class EnderComConfig {
    private static EnderComConfig INSTANCE;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "endercom.json";
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir()
            .resolve(CONFIG_FILE_NAME).toFile();

    private boolean enabled;
    private ChatGuardConfig chatGuardConfig;

    public EnderComConfig() {
        this.enabled = true;
        this.chatGuardConfig = ChatGuardConfig.get();
    }

    /**
     * Returns the EnderCom config instance.
     * @return Instance of EnderComConfig
     */
    public static EnderComConfig get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    /**
     * Loads the EnderCom config from the config file.
     */
    public static void load() {
        log.info("Loading EnderCom config");
        if (!CONFIG_FILE.exists()) {
            log.info("EnderCom config file does not exist, creating default config");
            createDefaultConfig();
            return;
        }
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            log.debug("Loading EnderCom config from {}", CONFIG_FILE.getPath());
            INSTANCE = GSON.fromJson(reader, EnderComConfig.class);
            log.debug("config values: {}", INSTANCE.toString());
        } catch (Exception e) {
            log.error("Failed to load EnderCom config", e);
            createDefaultConfig();
        }
    }

    /**
     * Creates a default EnderCom config and saves it to the config file.
     */
    private static void createDefaultConfig() {
        INSTANCE = new EnderComConfig();
        save();
        log.debug("default config values: {}", INSTANCE.toString());
    }

    /**
     * Saves the EnderCom config to the config file.
     */
    public static void save() {
        if (!CONFIG_FILE.getParentFile().exists()) createParentDirectory();

        try(FileWriter writer = new FileWriter(CONFIG_FILE)) {
            log.info("Saving EnderCom config to {}", CONFIG_FILE.getPath());
            GSON.toJson(INSTANCE, writer);
        } catch (Exception e) {
            log.error("Failed to save EnderCom config", e);
        }
    }

    public @NonNull ChatGuardConfig getChatGuardConfig() {
        return chatGuardConfig;
    }

    public void setChatGuardConfig(@NonNull ChatGuardConfig chatGuardConfig) {
        this.chatGuardConfig = chatGuardConfig;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public @NonNull String toString() {
        return "EnderComConfig{" +
                "enabled=" + enabled +
                ", chatGuardConfig=" + chatGuardConfig.toString() +
                '}';
    }

    private static void createParentDirectory() {
        log.info("EnderCom config parent directory does not exist, creating parent directory.");
        boolean directoryCreated = CONFIG_FILE.getParentFile().mkdirs();
        if (!directoryCreated) {
            log.warn("Failed to create directory for EnderCom config file.");
            log.warn("Continuing without EnderCom enabled.");
            INSTANCE.setEnabled(false);
        }
    }
}
