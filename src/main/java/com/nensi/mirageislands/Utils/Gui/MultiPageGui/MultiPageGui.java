package com.nensi.mirageislands.Utils.Gui.MultiPageGui;

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
import java.util.HashMap;
import java.util.List;

public class MultiPageGui {

    private long cd;

    private final HashMap<Integer, ItemStack> old_contents = new HashMap<>();

    protected final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);
    private final MPGuiManager mpgManager = new MPGuiManager();
    protected MPGuiMode mode = MPGuiMode.HIDE_BUTTONS;

    protected final Player p;
    protected final CustomConfig cfg;

    public Inventory gui;

    protected String id;
    protected String title;
    protected int size;
    protected int[] slots;

    protected int content_slots_length;
    protected int content_length;

    protected int current_page;

    public MultiPageGui(Player p, Configuration cfg) {

        this.p = p;

        this.cfg = new CustomConfig(cfg);

        this.id = "default";
        this.title = "Default";
        this.size = 9;

        this.slots = new int[]{};
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

    public int getMaxPage() {
        return  (int)Math.ceil((double) content_length / content_slots_length) - 1;
    }


    protected List<MPGContentItem> getContents() {
        return new ArrayList<>();
    }

    protected List<MPGContentItem> getPersistentItems() {
        return new ArrayList<>();
    }

    protected List<MPGContentItem> getReplacedArrows() {
        return new ArrayList<>();
    }


    @SuppressWarnings("deprecation")
    protected void createInventory() {
        try {

            gui = Bukkit.createInventory(null, size, plugin.utils.setColors(title));

            this.content_slots_length = slots.length;
            this.content_length = getContents().size();

        } catch (Exception ex) {
            ex.printStackTrace();

            gui = Bukkit.createInventory(null, 9, plugin.utils.setColors("&cError"));
        }
    }

    public void Update(int page) {
        current_page = page;
        clearContents();
        setContents(page);
        setPersistentItems();
    }

    public void smartUpdate(int page) {
        current_page = page;

        List<MPGContentItem> contentItems = getContents();
        if (contentItems.isEmpty()) return;

        if (cd > System.currentTimeMillis()) return;
        cd = System.currentTimeMillis() + 50;

        setPersistentItems();

        int item_index = content_slots_length * page;
        for (int i = 0; i < content_slots_length; i++) {
            if (item_index >= content_length) {
                return;
            }

            ItemStack newItem = contentItems.get(item_index).getItemStack();
            ItemStack oldItem = old_contents.getOrDefault(slots[i], new ItemStack(Material.AIR));
            int slot = slots[i];

            if (!oldItem.isSimilar(newItem)) {
                gui.setItem(slot, newItem);
                old_contents.put(slot, newItem);
            }

            item_index++;
        }
    }


    private void clearContents() {
        for (int slot : slots) {
            gui.setItem(slot, new ItemStack(Material.AIR));
        }
    }

    public void setContents(int page) {

        List<MPGContentItem> contentItems = getContents();

        if (contentItems.isEmpty()) return;
        int item_index = content_slots_length * page;
        for (int i = 0; i < content_slots_length; i++) {
            if (item_index >= content_length) break;
            gui.setItem(slots[i], contentItems.get(item_index).getItemStack());
            item_index++;
        }
    }


    public void setPersistentItems() {

        int max_page = Math.max(0, (int)Math.ceil((double) content_length / content_slots_length) - 1);
        List<MPGContentItem> replacedArrows = getReplacedArrows();

        for (MPGContentItem item : getPersistentItems()) {

            String id = item.getId();

            switch (id) {

                case "previous" -> {
                    if (mode == MPGuiMode.REPLACE_BUTTONS && (current_page == 0 || getContents().isEmpty()))
                        gui.setItem(replacedArrows.get(0).getSlot(), replacedArrows.get(0).getItemStack());
                }

                case "next" -> {
                    if (mode == MPGuiMode.REPLACE_BUTTONS && (current_page == max_page || getContents().isEmpty()))
                        gui.setItem(replacedArrows.get(1).getSlot(), replacedArrows.get(1).getItemStack());
                }

                default -> {
                    gui.setItem(item.getSlot(), item.getItemStack());
                    old_contents.put(item.getSlot(), item.getItemStack());
                }
            }

        }
    }

    public int getPage() {
        return current_page;
    }

    @SuppressWarnings("unused")
    public void Open(int page, boolean update) {
        try {

            createInventory();

            p.openInventory(gui);

            Update(page);

            if (update) Runnable();

            mpgManager.setGui(p, this);

            plugin.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void smartOpen(int page, boolean update) {
        try {

            createInventory();

            p.openInventory(gui);

            smartUpdate(page);

            if (update) Runnable();

            mpgManager.setGui(p, this);

            plugin.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

                    Update(getPage());

                } catch (Exception ex) {
                    this.cancel();
                }
            }

        }.runTaskTimer(plugin, 0, 2);
    }

    public void Close() {
        p.closeInventory();
        mpgManager.clearGui(p);
        plugin.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }

    @SuppressWarnings("unused")
    public void clickButton(String id, ClickType clickType, int page) {}
}
