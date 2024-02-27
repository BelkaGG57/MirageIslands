package com.nensi.mirageislands.Utils.Items;

import com.nensi.mirageislands.MirageIslands;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemHandler {

    @SuppressWarnings("deprecation")
    public ItemStack getItemTemplate(Material material, int cmd, String name, List<String> lore) {

        ItemStack itm = new ItemStack(material);
        ItemMeta meta = itm.getItemMeta();
        assert meta != null;


        meta.setCustomModelData(cmd);
        meta.setDisplayName(MirageIslands.utils.setColors(name));


        List<String> formatted_lore = new ArrayList<>();

        for (String line : lore) {
            formatted_lore.add(MirageIslands.utils.setColors(line));
        }

        meta.setLore(formatted_lore);

        itm.setItemMeta(meta);

        return itm;
    }

}
