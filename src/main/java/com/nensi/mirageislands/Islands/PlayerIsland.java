package com.nensi.mirageislands.Islands;

import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Player.PlayerDataManager;
import com.nensi.mirageislands.Utils.Config.CustomConfig;
import com.nensi.mirageislands.WorldHandler.WorldHandler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerIsland {

    private int index;
    private final String uuid;
    private final Island island;
    private List<String> expansions = new ArrayList<>();


    public PlayerIsland(int index, String uuid, Island island) {
        this.index = index;
        this.uuid = uuid;
        this.island = island;
    }

    public PlayerIsland(String uuid, String id, JSONObject jsonObject) {
        this.index = Integer.parseInt(jsonObject.get("index").toString());
        this.uuid = uuid;
        this.island = new IslandManager().getIsland(id);

        JSONArray jsonArray = (JSONArray) jsonObject.get("expansions");

        for (Object o : jsonArray) {
            expansions.add((String)o);
        }

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        new PlayerDataManager(uuid).setIsland(getIsland().getId(), this);
    }

    public String getUuid() {
        return uuid;
    }

    public Island getIsland() {
        return island;
    }

    public Location getPosition() {
        WorldHandler worldHandler = new WorldHandler();
        return worldHandler.getWithStep(index);
    }

    @SuppressWarnings("deprecation")
    public void Teleport() {
        Player p = Bukkit.getPlayer(UUID.fromString(uuid));

        if (p == null) return;

        Location loc = getPosition().clone();

        String[] str = getIsland().getConfig().getString("spawn_offset", "none").split(";");

        int x = Integer.parseInt(str[0]);
        int y = Integer.parseInt(str[1]);
        int z = Integer.parseInt(str[2]);

        p.teleport(loc.add(x, y, z));
        new IslandManager().setCurrentIsland(p, this);

        p.sendTitle(MirageIslands.utils.setColors("&aПеремещение!"), MirageIslands.utils.setColors("&7\"" + getIsland().getName() + "&7\""), 10, 30, 10);
        MirageIslands.sounds.clientSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1, true, false));

        startRunnable(p);
        startStatRunnable(p);
    }

    @SuppressWarnings("deprecation")
    public void Teleport(Player p) {

        if (p == null) return;

        Location loc = getPosition().clone();

        String[] str = getIsland().getConfig().getString("spawn_offset", "none").split(";");

        int x = Integer.parseInt(str[0]);
        int y = Integer.parseInt(str[1]);
        int z = Integer.parseInt(str[2]);

        p.teleport(loc.add(x, y, z));
        new IslandManager().setCurrentIsland(p, this);

        p.sendTitle(MirageIslands.utils.setColors("&aПеремещение!"), MirageIslands.utils.setColors("&7\"" + getIsland().getName() + "&7\""), 10, 30, 10);
        MirageIslands.sounds.clientSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1, true, false));

        startRunnable(p);
        startStatRunnable(p);
    }

    private void startRunnable(Player p) {

        new BukkitRunnable() {

            final IslandManager islandManager = new IslandManager();

            @Override
            public void run() {
                if (!p.isOnline() || !islandManager.isOnIsland(p) || p.isDead() || p.getLocation().getY() < -30) {

                    String[] parts = MirageIslands.configUtils.get("config").getString("return_loc", "none").split(";");
                    World world = Bukkit.getWorld(parts[0]);
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);

                    p.teleport(new Location(world, x, y, z));
                    islandManager.removeCurrent(p);

                    this.cancel();
                    return;
                }
                if (!p.getWorld().getName().equalsIgnoreCase(WorldHandler.WORLD_NAME)) {
                    islandManager.removeCurrent(p);
                    this.cancel();
                }

            }

        }.runTaskTimer(MirageIslands.getPlugin(MirageIslands.class), 0, 20);

    }

    private void startStatRunnable(Player p) {

        new BukkitRunnable() {

            final IslandManager islandManager = new IslandManager();

            @Override
            public void run() {
                if (!islandManager.isOnIsland(p) || p.isDead() || !p.isOnline()) {
                    this.cancel();
                    return;
                }

                new StatManager(p, getIsland()).Apply();

            }

        }.runTaskTimer(MirageIslands.getPlugin(MirageIslands.class), 0, getIsland().getStatInterval());

    }



    public List<String> getExpansions() {
        return expansions;
    }

    public void placeExpansions() {

        for (String id : getExpansions()) {
            if (getExpansion(id) == null) continue;
            getExpansion(id).Place();
        }
    }

    public IslandExpansion getExpansion(String id) {

        return new IslandExpansion(this, id, island.getConfig().getSection("expansions." + id, null));
    }

    @SuppressWarnings("unused")
    public void setExpansions(List<String> list) {
        expansions = list;
        new PlayerDataManager(uuid).setIsland(island.getId(), this);
    }

    public void addExpansion(String id) {
        expansions.add(id);
        new PlayerDataManager(uuid).setIsland(island.getId(), this);
    }

    public void removeExpansion(String id) {
        expansions.remove(id);
        new PlayerDataManager(uuid).setIsland(island.getId(), this);
    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemStack(Player p) {

        CustomConfig cfg = getIsland().getConfig();

        ItemStack itm = new ItemStack(Material.valueOf(cfg.getString("material", "BARRIER")));

        ItemMeta meta = itm.getItemMeta();
        meta.setDisplayName(MirageIslands.utils.setColors(cfg.getString("name", "test")));
        meta.setCustomModelData(cfg.getInteger("cmd", 1));

        List<String> lore = new ArrayList<>();


        lore.add(" ");


        lore.add(MirageIslands.utils.setColors("&fРазмер: " + getIsland().getSize().getName()));

        lore.add(" ");

        lore.add(MirageIslands.utils.setColors("&fРасширения: "));

        if (!getExpansions().isEmpty()) {
            for (String expansion : getExpansions()) {
                lore.add(MirageIslands.utils.setColors("  &7- " + getExpansion(expansion).getName()));
            }
        } else {
            lore.add(MirageIslands.utils.setColors("  &cНет "));
        }

        lore.add(" ");

        if (new IslandManager().isOnIsland(p)) {

            if (!new IslandManager().getCurrentIsland(p).getIsland().getId().equalsIgnoreCase(getIsland().getId())) {
                lore.add(MirageIslands.utils.setColors("&7ЛКМ - переместиться"));
            }

        } else {
            lore.add(MirageIslands.utils.setColors("&7ЛКМ - переместиться"));
        }

        if (!getIsland().getExpansions().isEmpty()) {

            lore.add(MirageIslands.utils.setColors("&7ПКМ - просмотр расширений"));

        }

        meta.setLore(lore);
        itm.setItemMeta(meta);

        return itm;
    }
}
