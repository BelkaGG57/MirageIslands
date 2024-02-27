package com.nensi.mirageislands.Utils.Gui.Default;

import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Utils.Gui.MultiPageGui.MPGuiManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;


public class GuiTemplateListener implements Listener {

    protected final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);
    private final GuiManager guiManager = new GuiManager();

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        guiManager.clearGui(p);
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (guiManager.isOpen(p) && !new MPGuiManager().isOpen(p)) {

            GuiTemplate gui = guiManager.getGui(p);

            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            e.setCancelled(true);

            ItemMeta meta = clickedItem.getItemMeta();
            String id = meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "id"), PersistentDataType.STRING, "none");


            if (id.equalsIgnoreCase("close")) {
                gui.Close();
            }

            gui.clickButton(id, e.getClick());

        }
    }

}
