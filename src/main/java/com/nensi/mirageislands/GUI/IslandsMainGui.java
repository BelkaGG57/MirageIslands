package com.nensi.mirageislands.GUI;

import com.nensi.mirageislands.Islands.Island;
import com.nensi.mirageislands.Islands.IslandManager;
import com.nensi.mirageislands.Islands.IslandSize;
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

public class IslandsMainGui extends MultiPageGui {

    private final ItemHandler itemHandler = new ItemHandler();
    private final IslandManager islandManager = new IslandManager();

    public IslandsMainGui(Player p) {

        super(p, null);
        super.id = "island_main_gui";

        super.title = MirageIslands.utils.setColors("&6Острова");
        super.size = 54;
        super.slots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 23, 24, 25, 26, 26, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
        super.mode = MPGuiMode.REPLACE_BUTTONS;

        createInventory();

    }

    @Override
    protected List<MPGContentItem> getContents() {

        List<MPGContentItem> results = new ArrayList<>();

        List<String> already = new ArrayList<>();

        for (Island island : islandManager.getIslands()) {

            if (already.contains(island.getMainId()) || island.getSize() != IslandSize.MEDIUM) continue;

            already.add(island.getMainId());

            results.add(new MPGContentItem(island.getMainItemStack(p),"island:" + island.getId(), 0));
        }

        return results;
    }

    @Override
    protected List<MPGContentItem> getPersistentItems() {

        List<MPGContentItem> results = new ArrayList<>();

        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.BARRIER, 10, "&cЗакрыть", List.of(" ", "&7Нажмите, чтобы закрыть меню")), "close", 48));

        if (islandManager.getPlayerIslandIds(p).isEmpty()) {
            results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.PAPER, 10, "&fВаши острова", List.of(" ", "&cУ вас пока нет островов")), "your_islands", 50));

        } else {
            results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.PAPER, 10, "&fВаши острова", List.of(" ", "&7Нажмите, чтобы посмотреть меню")), "your_islands", 50));
        }

        gui.setItem(52, new ItemStack(Material.AIR));
        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.ARROW, 7002, "&fСледующая страница", List.of(" ", "&7Нажмите, чтобы перейти на страницу")), "next", 52));
        gui.setItem(46, new ItemStack(Material.AIR));
        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.ARROW, 7001, "&fПредыдущая страница", List.of(" ", "&7Нажмите, чтобы перейти на страницу")), "previous", 46));

        return results;
    }

    @Override
    protected List<MPGContentItem> getReplacedArrows() {
        List<MPGContentItem> results = new ArrayList<>();
        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.RED_STAINED_GLASS_PANE, 7003, "&cСледующая страница", List.of(" ", "&7В данный момент недоступно")), "next", 52));
        results.add(new MPGContentItem(itemHandler.getItemTemplate(Material.RED_STAINED_GLASS_PANE, 7004, "&cПредыдущая страница", List.of(" ", "&7В данный момент недоступно")), "previous", 46));
        return results;
    }

    @Override
    public void clickButton(String id, ClickType clickType, int page) {

        if (id.startsWith("island")) {
            p.closeInventory();
            MirageIslands.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

            String island_id = id.split(":")[1];
            Island island = islandManager.getIsland(island_id);

            new IslandBuyGui(p, island).Open(false);

        }

        if (id.equalsIgnoreCase("your_islands")) {

            p.closeInventory();
            MirageIslands.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

            new PlayerIslandsGui(p).Open(0, true);
        }
    }
}
