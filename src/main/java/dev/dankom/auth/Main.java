package dev.dankom.auth;

import dev.dankom.auth.file.FileManager;
import dev.dankom.auth.listener.PlayerListener;
import dev.dankom.auth.logger.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static String PREFIX = "&a[&cAuth&6+&a]&r";
    private static FileManager fileManager;

    @Override
    public void onEnable() {
        Logger.log("Enabling " + PREFIX);

        fileManager = new FileManager();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        Logger.log("Disabling " + PREFIX);
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

    public static FileManager getFileManager() {
        return fileManager;
    }
}
