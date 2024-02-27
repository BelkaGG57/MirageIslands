package com.nensi.mirageislands.Player;

import com.nensi.mirageislands.Islands.Island;
import com.nensi.mirageislands.Islands.IslandManager;
import com.nensi.mirageislands.Islands.PlayerIsland;
import com.nensi.mirageislands.MirageIslands;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerDataManager {

    private final IslandManager islandManager = new IslandManager();

    private JSONObject jsonObject;
    private final String uuid;

    public PlayerDataManager(Player p) {
        this.uuid = p.getUniqueId().toString();

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader( MirageIslands.getPlugin(MirageIslands.class).getDataFolder().getAbsolutePath() + "/Data/players.json"));
            this.jsonObject = (JSONObject)(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!jsonObject.containsKey(uuid)) {
            JSONObject playerObject = new JSONObject();
            JSONObject islandsObject = new JSONObject();
            playerObject.put("islands", islandsObject);
            jsonObject.put(uuid, playerObject);
        }

    }

    public PlayerDataManager(String uuid) {
        this.uuid = uuid;
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader( MirageIslands.getPlugin(MirageIslands.class).getDataFolder().getAbsolutePath() + "/Data/players.json"));
            this.jsonObject = (JSONObject)(obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!jsonObject.containsKey(uuid)) {
            JSONObject playerObject = new JSONObject();
            JSONObject islandsObject = new JSONObject();
            playerObject.put("islands", islandsObject);
            jsonObject.put(uuid, playerObject);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public static List<PlayerDataManager> getManagers() {

        List<PlayerDataManager> list = new ArrayList<>();

        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader( MirageIslands.getPlugin(MirageIslands.class).getDataFolder().getAbsolutePath() + "/Data/players.json"));
            JSONObject jsonObject = (JSONObject)(obj);

            for (Object key : jsonObject.keySet()) {
                PlayerDataManager playerDataManager = new PlayerDataManager((String) key);
                list.add(playerDataManager);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return list;
    }

    public List<PlayerIsland> getIslands() {

        List<PlayerIsland> results = new ArrayList<>();

        JSONObject islandsObject = (JSONObject) ((JSONObject)jsonObject.get(uuid)).get("islands");

        for (Object key : islandsObject.keySet()) {
            for (Island island : islandManager.getIslands()) {

                if (island.getId().equalsIgnoreCase((String) key)) {

                    PlayerIsland playerIsland = new PlayerIsland(uuid, island.getId(), (JSONObject) islandsObject.get(key));

                    results.add(playerIsland);
                }

            }
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    public void setIslands(List<PlayerIsland> list) {

        JSONObject playerObject = (JSONObject) jsonObject.get(uuid);

        playerObject.put("islands", new JSONObject());

        JSONObject islandsObject = (JSONObject) playerObject.get("islands");

        if (!list.isEmpty()) {
            for (PlayerIsland island : list) {

                JSONObject islandObject = new JSONObject();

                islandObject.put("index", island.getIndex());
                islandObject.put("expansions", island.getExpansions());

                islandsObject.put(island.getIsland().getId(), islandObject);

            }
            playerObject.put("islands", islandsObject);
        }

        jsonObject.put(uuid, playerObject);

        try {
            FileWriter file = new FileWriter(MirageIslands.getPlugin(MirageIslands.class).getDataFolder().getAbsolutePath() + "/Data/players.json");
            file.write(jsonObject.toJSONString());
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void addIsland(PlayerIsland island) {

        List<PlayerIsland> islands = getIslands();
        islands.add(island);
        setIslands(islands);

    }

    public void removeIsland(PlayerIsland island) {

        List<PlayerIsland> islands = new ArrayList<>();

        for (PlayerIsland playerIsland : getIslands()) {
            if (!island.getIsland().getId().equalsIgnoreCase(playerIsland.getIsland().getId())) islands.add(playerIsland);
        }

        setIslands(islands);

    }

    public void setIsland(String id, PlayerIsland island) {

        List<PlayerIsland> islands = new ArrayList<>();

        for (PlayerIsland playerIsland : getIslands()) {
            if (!island.getIsland().getId().equalsIgnoreCase(playerIsland.getIsland().getId())) {
                islands.add(playerIsland);
            } else {
                islands.add(island);
            }
        }

        setIslands(islands);

    }


}
