package com.nensi.mirageislands.Utils.Gui.Default;

import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Utils.Config.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GuiTemplate {

    protected final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);
    protected final GuiManager guiManager = new GuiManager();

    protected final Player p;
    protected final CustomConfig cfg;

    protected Inventory gui;

    protected String id;
    protected String title;
    protected int size;

    public GuiTemplate(Player p, Configuration cfg) {

        this.p = p;
        this.cfg = new CustomConfig(cfg);

        this.id = "default";
        this.title = "Default";
        this.size = 9;

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    protected List<GuiContentItem> getContents() {
        return new ArrayList<>();
    }

    @SuppressWarnings({"deprecation"})
    protected void createInventory() {
        try {

            gui = Bukkit.createInventory(null, size, plugin.utils.setColors(title));
            Update();

        } catch (Exception ex) {
            ex.printStackTrace();

            gui = Bukkit.createInventory(null, 9, plugin.utils.setColors("&cError"));
        }
    }

    private void clearGui() {
        for (int i = 0; i < size; i++) {
            gui.setItem(i, new ItemStack(Material.AIR));
        }
    }

    @SuppressWarnings("unused")
    private void clearGui(int[] slots) {
        for (int i : slots) {
            gui.setItem(i, new ItemStack(Material.AIR));
        }
    }

    public void Update() {
        try {

            clearGui();
            setContents();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setContents() {
        for (GuiContentItem item : getContents()) {
            ItemStack itm = item.getItemStack();
            int slot = item.getSlot();
            gui.setItem(slot, itm);
        }
    }

    public void Open(boolean update) {

        createInventory();

        p.openInventory(gui);
        guiManager.setGui(p, this);

        if (update) Runnable();

        plugin.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }

    private void Runnable() {
        new BukkitRunnable() {

            @Override
            public void run() {
                try {

                    if (p.isDead() || !p.isValid() || !p.isOnline()) {
                        this.cancel();
                        return;
                    }

                    Update();

                } catch (Exception ex) {
                    this.cancel();
                }
            }

        }.runTaskTimer(plugin, 0, 2);
    }

    public void Close() {
        p.closeInventory();
        guiManager.clearGui(p);
        plugin.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }

    @SuppressWarnings("unused")
    public void clickButton(String id, ClickType clickType) {}

}
