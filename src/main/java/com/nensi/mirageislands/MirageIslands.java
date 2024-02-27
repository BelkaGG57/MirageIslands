package com.nensi.mirageislands;

import com.nensi.mirageislands.Commands.MainCommand;
import com.nensi.mirageislands.Islands.IslandManager;
import com.nensi.mirageislands.Listener.IslandListener;
import com.nensi.mirageislands.Utils.Config.ConfigUtils;
import com.nensi.mirageislands.Utils.Gui.Default.GuiTemplateListener;
import com.nensi.mirageislands.Utils.Gui.MultiPageGui.MPGuiListener;
import com.nensi.mirageislands.Utils.Sounds.SoundUtils;
import com.nensi.mirageislands.Utils.Utils;
import com.nensi.mirageislands.WorldHandler.WorldHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MirageIslands extends JavaPlugin {

    public static Utils utils;
    public static SoundUtils sounds;
    public static ConfigUtils configUtils;

    @Override
    public void onEnable() {

        utils = new Utils();
        sounds = new SoundUtils();
        configUtils = new ConfigUtils();

        setupCommands();
        loadConfigurations();

        getServer().getPluginManager().registerEvents(new MPGuiListener(), this);
        getServer().getPluginManager().registerEvents(new GuiTemplateListener(), this);
        getServer().getPluginManager().registerEvents(new IslandListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupCommands() {
        Objects.requireNonNull(getCommand("mirageislands")).setExecutor(new MainCommand());;

    }


    public void loadConfigurations() {

        configUtils.load("config.yml", "config");
        configUtils.loadResource("Types/test_small.yml");
        configUtils.loadResource("Types/test_medium.yml");
        configUtils.loadResource("Data/players.json");
        configUtils.loadResource("Schematics/test.schem");
        configUtils.loadResource("Schematics/test2.schem");
        configUtils.loadResource("Schematics/test2ex1.schem");
        new WorldHandler().createEmptyWorld();
        new IslandManager().Load();
    }


}
