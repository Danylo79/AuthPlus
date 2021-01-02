package dev.dankom.auth.file.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;

public class JsonFile {
    private File path;
    private String name;
    private JSONObject json;
    private JSONObject defaultValue;

    public JsonFile(File path, String name, JSONObject Default) {
        this.path = path;
        this.name = name + ".json";
        this.defaultValue = Default;
        this.json = this.defaultValue;

        generateConfig();
    }

    public void generateConfig() {
        if (!isGenerated()) {
            create();
        } else {
            update();
        }
    }

    public JSONObject getConfig() {
        if (!isGenerated()) {
            generateConfig();
        }
        File out = new File(path, getName());
        JSONParser parser = new JSONParser();

        try {
            return (JSONObject) parser.parse(new FileReader(out));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void create() {
        JSONObject obj = json;

        if (!path.exists()) {
            path.mkdirs();
        }

        try (FileWriter file = new FileWriter(new File(path, getName()))) {
            file.write(obj.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        JSONObject obj = getConfig();
        boolean updated = false;

        for(Iterator iterator = defaultValue.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            Object val = defaultValue.get(key);

            if (obj.containsKey(key)) {
                continue;
            } else {
                obj.put(key, val);
                updated = true;
            }
        }

        if (updated) {
            try (FileWriter file = new FileWriter(new File(path, getName()))) {
                file.write(obj.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isGenerated() {
        try {
            File main = new File(path, getName());
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(main));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void purge() {
        try (FileWriter file = new FileWriter(new File(path, getName()))) {
            file.write(defaultValue.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void save() {
        purge();
        generateConfig();
    }

    public void set(String key, Object value) {
        json.put(key, value);
        save();
    }

    public void addToArray(String array, Object o) {
        JSONArray jsonArray = (JSONArray) json.get(array);
        jsonArray.add(o);
        set(array, jsonArray);
    }

    public void addToArray(String array, JSONAware o) {
        JSONArray jsonArray = (JSONArray) json.get(array);
        jsonArray.add(o);
        set(array, jsonArray);
    }

    public void replaceInArray(String array, Object o, int index) {
        JSONArray jsonArray = (JSONArray) json.get(array);
        jsonArray.remove(index);
        jsonArray.add(o);
        set(array, jsonArray);
    }

    public void removeFromArray(String array, Object o) {
        JSONArray jsonArray = (JSONArray) json.get(array);
        for (int i = 0; i < jsonArray.size(); i++) {
            Object object = jsonArray.get(i);
            if (object.equals(o)) {
                jsonArray.remove(i);
                break;
            } else {
                continue;
            }
        }
        set(array, jsonArray);
    }

    public int getInArray(String array, String identifier, Object value) {
        JSONArray jsonArray = (JSONArray) json.get(array);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = (JSONObject) jsonArray.get(i);
            if (item.get(identifier) != null && item.get(identifier).equals(value)) {
                return i;
            } else {
                continue;
            }
        }
        return -1;
    }

    public int getInArray(String array, Object o) {
        JSONArray jsonArray = (JSONArray) json.get(array);
        for (int i = 0; i < jsonArray.size(); i++) {
            Object item = jsonArray.get(i);
            if (item.equals(o)) {
                return i;
            } else {
                continue;
            }
        }
        return -1;
    }

    public JSONArray getArray(String array) {
        return (JSONArray) json.get(array);
    }

    public void removeAllFromArray(String array) {
        JSONArray jsonArray = (JSONArray) json.get(array);
        jsonArray.clear();
        set(array, jsonArray);
    }
}
