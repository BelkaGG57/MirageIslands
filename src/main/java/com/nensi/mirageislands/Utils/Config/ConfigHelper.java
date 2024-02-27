package com.nensi.mirageislands.Utils.Config;

import com.nensi.mirageislands.MirageIslands;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class ConfigHelper {

    final HashMap<String, ConfigurationSection> loaded;
    final String path;

    public ConfigHelper(HashMap<String, ConfigurationSection> loaded, String path) {
        this.loaded = loaded;
        this.path = "plugins/MirageIslands/" + path;

        loadFromFolder(this.path);
    }

    private void loadFromFolder(String path) {
        try {

            File file = new File(path);

            if (file.exists() && file.isDirectory()) {

                File[] files = file.listFiles();

                if (files == null || files.length == 0) return;

                for (File f : files) {

                    if (f.isDirectory()) {
                        loadFromFolder(f.getPath());
                        continue;
                    }

                    Configuration cfg = YamlConfiguration.loadConfiguration(f);

                    for (String house : Objects.requireNonNull(cfg.getConfigurationSection("")).getKeys(false)) {
                        loaded.put(house, cfg.getConfigurationSection(house));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<String, ConfigurationSection> getHashMap() {
        return loaded;
    }

    public String[] getKeys() {
        return loaded.keySet().toArray(new String[0]);
    }

    public ConfigurationSection getSection(String id) {
        return loaded.getOrDefault(id, null);
    }


    private static long getFolderSize(File folder) {
        long length = 0;
        File[] files = folder.listFiles();

        int count = files.length;

        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            }
            else {
                length += getFolderSize(files[i]);
            }
        }
        return length;
    }



}
