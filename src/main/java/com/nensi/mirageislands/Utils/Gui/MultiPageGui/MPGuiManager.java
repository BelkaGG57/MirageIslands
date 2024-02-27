package com.nensi.mirageislands.Utils.Gui.MultiPageGui;

import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Utils.Config.CustomConfigSection;
import com.nensi.mirageislands.Utils.Items.LoreManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MPGuiManager {

    private final LoreManager loreManager = new LoreManager();

    public static HashMap<UUID, MultiPageGui> gui_open = new HashMap<>();

    public boolean isOpen(Player p) {
        return gui_open.containsKey(p.getUniqueId());
    }

    public void setGui(Player p, MultiPageGui gui) {
        gui_open.put(p.getUniqueId(), gui);
    }

    public void clearGui(Player p) {
        gui_open.remove(p.getUniqueId());
    }

    public MultiPageGui getGui(Player p) {
        return gui_open.getOrDefault(p.getUniqueId(), null);
    }




    @SuppressWarnings("deprecation")
    public MPGContentItem getMPGContentItem(CustomConfigSection fantasyConfigSection, String id) {

        Material material = Material.valueOf(fantasyConfigSection.getString("material", "BARRIER"));
        ItemStack itm = new ItemStack(material);
        ItemMeta meta = itm.getItemMeta();
        assert meta != null;

        int cmd = fantasyConfigSection.getInteger("custom_model_data", 0);
        meta.setCustomModelData(cmd);

        String name = MirageIslands.utils.setColors(fantasyConfigSection.getString("name", "&cNONE"));
        meta.setDisplayName(name);

        List<String> lore = new ArrayList<>(loreManager.getLore(fantasyConfigSection));

        meta.setLore(lore);
        itm.setItemMeta(meta);

        int slot = fantasyConfigSection.getInteger("slot", 0);

        return new MPGContentItem(itm, id, slot);
    }

}
