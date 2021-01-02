package dev.dankom.auth.file.yml;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigFile {
    private File dataFolder;
    private Logger logger;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private JavaPlugin plugin;
    private String fileName;

    public ConfigFile(String fileName, JavaPlugin plugin) {
        this.dataFolder = plugin.getDataFolder();
        this.logger = plugin.getLogger();
        this.fileName = fileName + ".yml";
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(dataFolder, fileName);
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void set(String key, Object value) {
        logger.log(Level.INFO, getConfig().toString());
        this.dataConfig.set(key, value);
        this.saveConfig();
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            reloadConfig();
        return this.dataConfig;
    }

    public String getString(String path) {
        return this.getConfig().getString(path);
    }

    public Integer getInt(String path) {
        return getConfig().getInt(path);
    }

    public Boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    public List<?> getList(String path) {
        return getConfig().getList(path);
    }

    public Object get(String path) {
        return getConfig().get(path);
    }

    public void saveConfig() {
//        logger.log(Level.INFO, "Config Saving!");
        if (this.dataConfig == null || this.configFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    private void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(dataFolder, this.fileName);
        }

        if (!this.configFile.exists()) {
            logger.log(Level.INFO, "Saved Default Config!");
            saveResource(dataFolder, this.fileName, false);
        }
    }

    public void saveResource(File dataFolder, String resourcePath, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = plugin.getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in ");
        }

        File outFile = new File(dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                logger.log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }
}
