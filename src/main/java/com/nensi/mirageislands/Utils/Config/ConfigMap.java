package com.nensi.mirageislands.Utils.Config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class ConfigMap {

    final HashMap<String, Configuration> loaded;
    final String main_path;
    final String path;

    public ConfigMap(HashMap<String, Configuration> loaded, String namespace, String main_path, String path) {
        this.loaded = loaded;
        this.main_path =  "plugins\\\\" + namespace + "\\\\" + main_path;
        this.path = "plugins/" + namespace + "/" + path;

        loadFromFolder(this.main_path + "\\\\", this.path);
    }

    public ConfigMap(HashMap<String, Configuration> loaded, String main_path, String path) {
        this.loaded = loaded;
        this.main_path =  "plugins\\\\MirageIslands\\\\" + main_path;
        this.path = "plugins/MirageIslands/" + path;

        loadFromFolder(this.main_path + "\\\\", this.path);
    }

    private void loadFromFolder(String main_path, String path) {
        try {

            File file = new File(path);

            if (file.exists() && file.isDirectory()) {

                File[] files = file.listFiles();

                if (files == null || files.length == 0) return;

                for (File f : files) {

                    if (f.isDirectory()) {
                        loadFromFolder(main_path, f.getPath());
                        continue;
                    }

                    String name = f.getName().replaceAll(".yml", "");
                    String file_path = f.getPath().replaceAll("/", "\\\\").replaceAll("/", "\\\\\\\\").
                            replaceAll(main_path, "").
                            replaceAll(f.getName(), "");
                    Configuration cfg = YamlConfiguration.loadConfiguration(f);

                    loaded.put(file_path + name, cfg);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<String, Configuration> getHashMap() {
        return loaded;
    }

    public String[] getKeys() {
        return loaded.keySet().toArray(new String[0]);
    }

    public Configuration getConfig(String id) {
        return loaded.getOrDefault(id, null);
    }




}
