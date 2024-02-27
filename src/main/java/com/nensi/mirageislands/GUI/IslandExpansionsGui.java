package com.nensi.mirageislands.GUI;

import com.nensi.mirageislands.Islands.IslandExpansion;
import com.nensi.mirageislands.Islands.IslandManager;
import com.nensi.mirageislands.Islands.PlayerIsland;
import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Utils.Gui.MultiPageGui.MPGContentItem;
import com.nensi.mirageislands.Utils.Gui.MultiPageGui.MPGuiMode;
import com.nensi.mirageislands.Utils.Gui.MultiPageGui.MultiPageGui;
import com.nensi.mirageislands.Utils.Items.ItemHandler;
import com.nensi.mirageislands.Utils.MoneyManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IslandExpansionsGui extends MultiPageGui {

    private final ItemHandler itemHandler = new ItemHandler();
    private final IslandManager islandManager = new IslandManager();

    private final PlayerIsland island;

    public IslandExpansionsGui(Player p, PlayerIsland island) {

        super(p, null);

        this.island = island;

        super.id = "island_expansions_gui";

        super.title = MirageIslands.utils.setColors("&6Расширения острова");
        super.size = 36;
        super.slots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 23, 24, 25, 26, 26};
        super.mode = MPGuiMode.REPLACE_BUTTONS;

        createInventory();

    }

    @Override
    protected List<MPGContentItem> getContents() {

        List<MPGContentItem> results = new ArrayList<>();

        for (String id : island.getIsland().getExpansions()) {
            results.add(new MPGContentItem(island.getExpansion(id).getItemStack(p),"expansion:" + id, 0));

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

            new PlayerIslandsGui(p).Open(0, false);
        }

        if (id.startsWith("expansion")) {

            String exp_id = id.split(":")[1];


            if (island.getExpansions().contains(exp_id)) {

                p.sendMessage(MirageIslands.utils.setColors("&cВы уже купили это расширение!"));
                MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);


            } else {

                IslandExpansion expansion = island.getExpansion(exp_id);

                if (new MoneyManager(p).countPlayerMoney() < expansion.getCost()) {
                    p.sendMessage(MirageIslands.utils.setColors("&cНедостаточно средств для покупки!"));
                    MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
                    return;
                }

                new MoneyManager(p).takeMoney(expansion.getCost());

                p.sendMessage(MirageIslands.utils.setColors("&aРасширение купленно!"));
                MirageIslands.sounds.clientSound(p, Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);
                MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                island.addExpansion(exp_id);
                islandManager.placeIslands();

                p.closeInventory();
                MirageIslands.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                new PlayerIslandsGui(p).Open(0, false);

            }

        }
    }
}
