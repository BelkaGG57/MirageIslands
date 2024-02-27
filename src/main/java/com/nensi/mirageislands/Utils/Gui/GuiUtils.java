package com.nensi.mirageislands.Utils.Gui;

import com.nensi.mirageislands.MirageIslands;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GuiUtils {

    private final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);


    public boolean checkLocked(ItemStack itm) {

        ItemMeta meta = itm.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "locked");
        return meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER) &&
                meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER).equals(1);

    }

}
