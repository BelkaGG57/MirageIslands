package com.nensi.mirageislands.Islands;

import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Player.PlayerDataManager;
import com.nensi.mirageislands.Schematics.SchematicManager;
import com.nensi.mirageislands.Utils.Config.ConfigMap;
import com.nensi.mirageislands.Utils.Config.CustomConfig;
import com.nensi.mirageislands.WorldHandler.WorldHandler;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class IslandManager {

    private final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);

    private static final HashMap<String, Island> islands = new HashMap<>();

    public static int placed_amount = 0;
    private static final HashSet<PlayerIsland> player_islands = new HashSet<>();
    private static final HashMap<UUID, PlayerIsland> current_islands = new HashMap<>();


    public void Load() {

        islands.clear();

        MirageIslands.configUtils.getConfigMaps().put("islands", new ConfigMap(new HashMap<>(), "Types", "Types"));

        for (String key : MirageIslands.configUtils.getConfigMaps().get("islands").getKeys()) {

            CustomConfig customConfig = new CustomConfig(MirageIslands.configUtils.getConfigMaps().get("islands").getConfig(key));
            islands.put(key, new Island(key, customConfig));

        }

        placeIslands();
    }

    public boolean islandExists(String id) {

        return islands.containsKey(id);

    }

    public List<Island> getIslands() {
        List<Island> results = new ArrayList<>();
        for (String key : islands.keySet()) {
            results.add(islands.get(key));
        }
        return results;
    }

    public List<String> getIslandIds() {
        List<String> results = new ArrayList<>();
        for (Island island : getIslands()) {
            results.add(island.getId());
        }
        return results;
    }

    public Island getIsland(String id) {
        return islands.get(id);
    }

    public int getPlacedAmount() {
        return placed_amount;
    }

    public void placeIslands() {

        HashMap<UUID, PlayerIsland> current_copy = (HashMap<UUID, PlayerIsland>) current_islands.clone();
        current_islands.clear();

        if (placed_amount > 0) {
            for (PlayerIsland playerIsland : player_islands) {
                new SchematicManager().removeSchematic(new WorldHandler().getWithStep(playerIsland.getIndex()), playerIsland.getIsland().getSchematic());

                try {
                    for (String id : playerIsland.getIsland().getExpansions()) {

                        IslandExpansion islandExpansion = playerIsland.getExpansion(id);

                        if (islandExpansion == null) continue;

                        new SchematicManager().removeSchematic(islandExpansion.getOffset(), islandExpansion.getSchematic());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

        player_islands.clear();

        placed_amount = 0;

        for (PlayerDataManager manager : PlayerDataManager.getManagers()) {

            for (PlayerIsland playerIsland : manager.getIslands()) {


                player_islands.add(playerIsland);

                playerIsland.getIsland().Place();
                playerIsland.setIndex(placed_amount);

                playerIsland.placeExpansions();

                if (current_copy.containsKey(UUID.fromString(manager.getUuid()))) {

                    if (current_copy.get(UUID.fromString(manager.getUuid())).getIsland().getId().equalsIgnoreCase(playerIsland.getIsland().getId())) {

                        Player target = Bukkit.getPlayer(UUID.fromString(manager.getUuid()));
                        if (target == null) continue;


                        int current_index = current_copy.getOrDefault(target.getUniqueId(), null).getIndex();
                        int index = playerIsland.getIndex();


                        Location loc = target.getLocation();

                        int delta = index == current_index ? 0 : index < current_index ? -(WorldHandler.DISTANCE) * (current_index - index) : (WorldHandler.DISTANCE) * (Math.max(1, index - current_index));


                        loc.add(delta,0 ,0);

                        target.teleport(loc);
                        current_islands.put(target.getUniqueId(), playerIsland);

                    }
                }

                placed_amount++;
            }
        }

    }

    public List<PlayerIsland> getPlayerIslands(Player p) {

        List<PlayerIsland> results = new ArrayList<>();

        for (PlayerIsland playerIsland : player_islands) {
            if (playerIsland.getUuid().equalsIgnoreCase(p.getUniqueId().toString())) {
                results.add(playerIsland);
            }
        }

        return results;

    }

    public List<String> getPlayerIslandIds(Player p) {

        List<String> results = new ArrayList<>();

        for (PlayerIsland playerIsland : getPlayerIslands(p)) {
            results.add(playerIsland.getIsland().getId());
        }

        return results;

    }

    public boolean hasIsland(Player p, String id) {
        return getPlayerIslandIds(p).contains(id);
    }

    public PlayerIsland getPlayerIsland(Player p, String id) {

        for (PlayerIsland playerIsland : getPlayerIslands(p)) {
            if (playerIsland.getIsland().getId().equalsIgnoreCase(id)) return playerIsland;
        }

        return null;

    }


    public PlayerIsland getCurrentIsland(Player p) {
        if (current_islands.getOrDefault(p.getUniqueId(), null) != null) return current_islands.getOrDefault(p.getUniqueId(), null);
        return null;
    }

    public boolean isOnIsland(Player p) {
        return current_islands.containsKey(p.getUniqueId());
    }

    public void setCurrentIsland(Player p, PlayerIsland island) {
        current_islands.put(p.getUniqueId(), island);
    }

    public void removeCurrent(Player p) {
        current_islands.remove(p.getUniqueId());
    }



}
