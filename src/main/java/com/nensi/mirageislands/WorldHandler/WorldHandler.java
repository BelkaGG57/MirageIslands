package com.nensi.mirageislands.WorldHandler;

import org.bukkit.*;

import java.io.File;
import java.io.IOException;

public class WorldHandler {

    public static final String WORLD_NAME = "mirageIslands";
    public static final int DISTANCE = 100;


    public boolean exists() {
        return Bukkit.getServer().getWorld(WORLD_NAME) != null;
    }

    public void createEmptyWorld() {

        if (exists()) deleteWorld();
        try {
            WorldCreator wc = new WorldCreator(WORLD_NAME);

            wc.type(WorldType.FLAT);
            wc.generateStructures(false);

            wc.generator(new EmptyChunkGenerator());

            World world = wc.createWorld();

            assert world != null;

            world.setDifficulty(Difficulty.PEACEFUL);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            world.setGameRule(GameRule.DO_INSOMNIA, false);
            world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        } catch (Exception ignore) {

        }
    }

    public World getWorld() {
        return exists() ? Bukkit.getServer().getWorld(WORLD_NAME) : null;
    }

    public Location getCenter() {
        return new Location(getWorld(), 0, 20, 0);
    }

    public Location getWithStep(int step) {
        return getCenter().add(step * DISTANCE, 0, 0);
    }


    @SuppressWarnings("all")
    public void deleteWorld() {

        try {
            File folder = getWorld().getWorldFolder();
            Bukkit.unloadWorld(getWorld(), false);

            try {
                org.apache.commons.io.FileUtils.deleteDirectory(folder);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ignore) {

        }

    }

}
