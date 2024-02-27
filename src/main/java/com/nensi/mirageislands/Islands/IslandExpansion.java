package com.nensi.mirageislands.Islands;

import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Schematics.SchematicManager;
import com.nensi.mirageislands.Utils.Config.CustomConfigSection;
import com.nensi.mirageislands.WorldHandler.WorldHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class IslandExpansion {

    private final PlayerIsland island;
    private final String id;
    private final CustomConfigSection section;


    public IslandExpansion(PlayerIsland island, String id, CustomConfigSection section) {
        this.island = island;
        this.id = id;
        this.section = section;
    }

    public String getId() {
        return id;
    }

    public String getSchematic() {
        return section.getString("schematic", "none");
    }

    public String getName() {
        return MirageIslands.utils.setColors(section.getString("name", "none"));
    }

    public int getCost() {
        return section.getInteger("cost", 100);
    }

    public Location getOffset() {

        WorldHandler worldHandler = new WorldHandler();

        Location loc = island.getPosition();

        String[] str = section.getString("offset", "none").split(";");

        int x = Integer.parseInt(str[0]);
        int y = Integer.parseInt(str[1]);
        int z = Integer.parseInt(str[2]);

        loc.add(x, y, z);

        return loc;

    }

    public void Place() {

        SchematicManager schematicManager = new SchematicManager();

        schematicManager.placeSchematic(getOffset(), getSchematic());

    }

    @SuppressWarnings("deprecation")
    public ItemStack getItemStack(Player p) {

        ItemStack itm = new ItemStack(Material.valueOf(section.getString("material", "BARRIER")));

        ItemMeta meta = itm.getItemMeta();
        meta.setDisplayName(getName());
        meta.setCustomModelData(section.getInteger("cmd", 1));

        List<String> lore = new ArrayList<>();

        lore.add(" ");

        lore.add(MirageIslands.utils.setColors("&fЦена: &e" + getCost() + "$"));

        lore.add(" ");

        if (island.getExpansions().contains(id)) {

            lore.add(MirageIslands.utils.setColors("&cУже куплено!"));
        } else {

            lore.add(MirageIslands.utils.setColors("&7Нажмите, чтобы купить"));
        }


        meta.setLore(lore);
        itm.setItemMeta(meta);

        return itm;
    }
}
