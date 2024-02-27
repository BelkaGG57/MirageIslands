package com.nensi.mirageislands.Islands;

import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Schematics.SchematicManager;
import com.nensi.mirageislands.Utils.Config.CustomConfig;
import com.nensi.mirageislands.WorldHandler.WorldHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Island {

    private final String id;
    private final CustomConfig cfg;

    public Island(String id, CustomConfig cfg) {
        this.id = id;
        this.cfg = cfg;
    }

    public CustomConfig getConfig() {
        return cfg;
    }

    public String getId() {
        return id;
    }

    public String getMainId() {
        return id.split("_")[0];
    }

    public IslandSize getSize() {

        String str = id.split("_")[1].toUpperCase();

        if (IslandSize.valueOf(str) == null) return IslandSize.SMALL;

        return IslandSize.valueOf(str);
    }

    public String getName() {
        return cfg.getString("name", "none");
    }

    public int getCost() {
        return cfg.getInteger("cost", 100);
    }

    public int getStatInterval() {
        return cfg.getInteger("stats_interval", 100);
    }

    public String getSchematic() {
        return cfg.getString("schematic", "none");
    }

    public void Place() {

        IslandManager islandManager = new IslandManager();
        WorldHandler worldHandler = new WorldHandler();
        SchematicManager schematicManager = new SchematicManager();

        schematicManager.placeSchematic(worldHandler.getWithStep(islandManager.getPlacedAmount()), getSchematic());

    }


    public List<String> getExpansions() {

        return cfg.getSection("expansions", null).getSourceSection() == null ? new ArrayList<>() : new ArrayList<>(cfg.getSection("expansions", null).getSourceSection().getKeys(false));

    }


    @SuppressWarnings("deprecation")
    public ItemStack getMainItemStack(Player p) {

        ItemStack itm = new ItemStack(Material.valueOf(cfg.getString("material", "BARRIER")));

        ItemMeta meta = itm.getItemMeta();
        meta.setDisplayName(MirageIslands.utils.setColors(cfg.getString("name", "test")));
        meta.setCustomModelData(cfg.getInteger("cmd", 1));

        List<String> lore = new ArrayList<>();

        lore.add(" ");

        lore.add(" ");

        lore.add(MirageIslands.utils.setColors("&fРазмеры:"));

        int bought = 0;
        int max = 0;

        for (IslandSize size : IslandSize.values()) {

            String size_id = getMainId() + "_" + size.toString().toLowerCase();

            if (!new IslandManager().getIslandIds().contains(size_id)) continue;

            max++;

            if (new IslandManager().getPlayerIslandIds(p).contains(size_id)) {
                lore.add(MirageIslands.utils.setColors(" &7- " + size.getName() + " &7(&aДоступно&7)"));
                bought++;
            } else {
                lore.add(MirageIslands.utils.setColors(" &7- " + size.getName() + " &7(&cНедоступно&7)"));
            }


        }

        lore.add(" ");


        if (bought >= max) {

            lore.add(MirageIslands.utils.setColors("&7Все куплено!"));
        } else {

            lore.add(MirageIslands.utils.setColors("&7Нажмите, чтобы купить"));
        }




        meta.setLore(lore);


        itm.setItemMeta(meta);

        return itm;
    }


}
