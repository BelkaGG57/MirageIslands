package com.nensi.mirageislands.GUI;

import com.nensi.mirageislands.Islands.IslandManager;
import com.nensi.mirageislands.Islands.PlayerIsland;
import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Utils.Gui.MultiPageGui.MPGContentItem;
import com.nensi.mirageislands.Utils.Gui.MultiPageGui.MPGuiMode;
import com.nensi.mirageislands.Utils.Gui.MultiPageGui.MultiPageGui;
import com.nensi.mirageislands.Utils.Items.ItemHandler;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerIslandsGui extends MultiPageGui {

    private final ItemHandler itemHandler = new ItemHandler();
    private final IslandManager islandManager = new IslandManager();

    public PlayerIslandsGui(Player p) {

        super(p, null);
        super.id = "island_player_gui";

        super.title = MirageIslands.utils.setColors("&6Ваши острова");
        super.size = 36;
        super.slots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 23, 24, 25, 26, 26};
        super.mode = MPGuiMode.REPLACE_BUTTONS;

        createInventory();

    }

    @Override
    protected List<MPGContentItem> getContents() {

        List<MPGContentItem> results = new ArrayList<>();

        for (PlayerIsland island : islandManager.getPlayerIslands(p)) {

            results.add(new MPGContentItem(island.getItemStack(p),"island:" + island.getIsland().getId(), 0));
        }

        return results;
    }

    @Override
    protected List<MPGContentItem> getPersistentItems() {

        List<MPGContentItem> results = new ArrayList<>();

        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.BARRIER, 10, "&cНазад", List.of(" ", "&7Нажмите, чтобы вернуться обратно")), "back", 31));

        gui.setItem(33, new ItemStack(Material.AIR));
        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.ARROW, 7002, "&fСледующая страница", List.of(" ", "&7Нажмите, чтобы перейти на страницу")), "next", 33));
        gui.setItem(29, new ItemStack(Material.AIR));
        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.ARROW, 7001, "&fПредыдущая страница", List.of(" ", "&7Нажмите, чтобы перейти на страницу")), "previous", 29));

        return results;
    }

    @Override
    protected List<MPGContentItem> getReplacedArrows() {
        List<MPGContentItem> results = new ArrayList<>();
        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.RED_STAINED_GLASS_PANE, 7003, "&cСледующая страница", List.of(" ", "&7В данный момент недоступно")), "next", 33));
        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.RED_STAINED_GLASS_PANE, 7004, "&cПредыдущая страница", List.of(" ", "&7В данный момент недоступно")), "previous", 29));
        return results;
    }

    @Override
    public void clickButton(String id, ClickType clickType, int page) {

        if (id.equalsIgnoreCase("back")) {
            p.closeInventory();
            MirageIslands.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

            new IslandsMainGui(p).Open(0, false);
        }

        if (id.startsWith("island")) {

            String island_id = id.split(":")[1];
            PlayerIsland playerIsland = islandManager.getPlayerIsland(p, island_id);

            if (clickType.isLeftClick()) {
                if (islandManager.isOnIsland(p)) {

                    if (!islandManager.getCurrentIsland(p).getIsland().getId().equalsIgnoreCase(playerIsland.getIsland().getId())) {
                        p.closeInventory();
                        MirageIslands.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                        playerIsland.Teleport();
                    }

                } else {
                    p.closeInventory();
                    MirageIslands.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                    playerIsland.Teleport();
                }
            } else if (clickType.isRightClick()) {

                if (playerIsland.getIsland().getExpansions().isEmpty()) return;

                p.closeInventory();
                MirageIslands.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                new IslandExpansionsGui(p, playerIsland).Open(0, false);

            }

        }
    }
}
