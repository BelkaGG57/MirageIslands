package com.nensi.mirageislands.Utils.Gui.Default;

import com.nensi.mirageislands.MirageIslands;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GuiContentItem {

    final ItemStack itm;
    final String id;
    final int slot;

    public GuiContentItem(ItemStack itm, String id, int slot) {
        this.itm = itm;
        this.id = id;
        this.slot = slot;
        setId();
    }

    private void setId() {

        ItemMeta meta = itm.getItemMeta();

        meta.getPersistentDataContainer().set(new NamespacedKey(MirageIslands.getPlugin(MirageIslands.class), "id"), PersistentDataType.STRING, id);

        itm.setItemMeta(meta);
    }

    public String getId() {
        return id;
    }

    public ItemStack getItemStack() {
        return itm;
    }

    public int getSlot() {
        return slot;
    }

}
