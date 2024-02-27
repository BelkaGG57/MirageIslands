package com.nensi.mirageislands.Utils.Config;

import com.nensi.mirageislands.MirageIslands;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class ConfigUtils {

    private final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);

    public static HashMap<String, Configuration> loaded = new HashMap<>();
    public static HashMap<String, ConfigMap> configMaps = new HashMap<>();

    public HashMap<String, ConfigMap> getConfigMaps() {
        return configMaps;
    }

    @SuppressWarnings("unused")
    public void clear() {
        loaded.clear();
    }

    @SuppressWarnings("all")
    public void load(String path, String id) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File file = new File(plugin.getDataFolder(), path);
        if (!file.exists()) {
            plugin.saveResource(path, false);
        }

        Configuration cfg = YamlConfiguration.loadConfiguration(file);
        loaded.remove(id);
        loaded.put(id, cfg);
    }

    public Configuration get(String id) {
        for (String key : loaded.keySet()) {
            if (key.equalsIgnoreCase(id)) {
                return loaded.getOrDefault(key, null);
            }
        }
        return null;
    }

    @SuppressWarnings("all")
    public void loadResource(String path, String id) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File file = new File(plugin.getDataFolder().getAbsolutePath(), path + "/" + id );
        if (!file.exists()) {
            plugin.saveResource(path + "/" + id, false);
        }
    }

    @SuppressWarnings("all")
    public void loadResource(String id) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File file = new File(plugin.getDataFolder().getAbsolutePath(), id);
        if (!file.exists()) {
            plugin.saveResource(id, false);
        }
    }

}
