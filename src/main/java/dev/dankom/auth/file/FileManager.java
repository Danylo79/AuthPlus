package dev.dankom.auth.file;

import dev.dankom.auth.Main;
import dev.dankom.auth.file.json.JsonFile;
import dev.dankom.auth.file.json.JsonObjectBuilder;
import dev.dankom.auth.file.yml.ConfigFile;
import dev.dankom.auth.logger.Logger;

import java.util.ArrayList;

public class FileManager {
    public ConfigFile config = new ConfigFile("config", Main.getInstance());
    public JsonFile players = new JsonFile(Main.getInstance().getDataFolder(), "player_data", new JsonObjectBuilder().addArray("profiles", new ArrayList<>()).build());

    public FileManager() {
        Logger.log("Initializing FileManager");
    }
}
