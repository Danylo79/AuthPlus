package dev.dankom.auth.profile;

import dev.dankom.auth.Main;
import dev.dankom.auth.file.json.JsonObjectBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile {
    private final Player p;

    public Profile(UUID id) {
        this(Bukkit.getPlayer(id));
    }

    public Profile(String name) {
        this(Bukkit.getPlayer(name));
    }

    public Profile(Player p) {
        this.p = p;
        if (database() == null) {
            addProfileData();
        }
        updateProfileData();
    }

    public void updateProfileData() {
        JSONObject json = Main.getFileManager().players.getConfig();
        JSONArray array = (JSONArray) json.get("profiles");
        for (Object o : array.toArray()) {
            JSONObject object = (JSONObject) o;
            if (((String) object.get("uuid")).equalsIgnoreCase(getPlayer().getUniqueId().toString())) {
                removePlayerData(array, object);
            }
        }
        addProfileData();
    }

    public void removePlayerData(JSONArray array, JSONObject object) {
        JSONArray jsonArray = array;
        jsonArray.remove(object);
        Main.getFileManager().players.set("profiles", jsonArray);
        save();
    }

    public void addProfileData() {
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.addKeyValuePair("uuid", getPlayer().getUniqueId().toString());
        builder.addKeyValuePair("username", getPlayer().getDisplayName());
        Main.getFileManager().players.addToArray("profiles", builder.build());
        save();
    }

    public Player getPlayer() {
        return p;
    }

    public JSONObject database() {
        JSONObject json = Main.getFileManager().players.getConfig();
        JSONArray array = (JSONArray) json.get("profiles");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            if (((String) object.get("uuid")).equalsIgnoreCase(getPlayer().getUniqueId().toString())) {
                return object;
            }
        }
        return null;
    }

    public void save() {
        Main.getFileManager().players.save();
    }
}
