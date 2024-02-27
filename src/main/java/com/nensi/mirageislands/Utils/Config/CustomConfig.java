package com.nensi.mirageislands.Utils.Config;

import org.bukkit.configuration.Configuration;

import java.util.List;

public class CustomConfig {

    private final Configuration cfg;

    public CustomConfig(Configuration cfg) {
        this.cfg = cfg;
    }

    public Configuration getConfig() {
        return cfg;
    }


    public String getString(String path, String default_value) {
        try {
            return cfg.getString(path);
        } catch (NullPointerException ex) {
            return default_value;
        }
    }

    public List<String> getStringList(String path, List<String> default_value) {
        try {
            return cfg.getStringList(path);
        } catch (NullPointerException ex) {
            return default_value;
        }
    }

    public int getInteger(String path, int default_value) {
        try {
            return cfg.getInt(path);
        } catch (NullPointerException ex) {
            return default_value;
        }
    }

    public List<Integer> getIntegerList(String path, List<Integer> default_value) {
        try {
            return cfg.getIntegerList(path);
        } catch (NullPointerException ex) {
            return default_value;
        }
    }

    public double getDouble(String path, double default_value) {
        try {
            return cfg.getDouble(path);
        } catch (NullPointerException ex) {
            return default_value;
        }
    }

    public boolean getBoolean(String path, boolean default_value) {
        try {
            return cfg.getBoolean(path);
        } catch (NullPointerException ex) {
            return default_value;
        }
    }

    public CustomConfigSection getSection(String path, CustomConfigSection default_value) {
        try {
            return new CustomConfigSection(cfg.getConfigurationSection(path));
        } catch (NullPointerException ex) {
            return default_value;
        }
    }
}
