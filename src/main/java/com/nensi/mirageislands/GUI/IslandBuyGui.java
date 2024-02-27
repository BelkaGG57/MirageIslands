package com.nensi.mirageislands.GUI;

import com.nensi.mirageislands.Islands.Island;
import com.nensi.mirageislands.Islands.IslandManager;
import com.nensi.mirageislands.Islands.IslandSize;
import com.nensi.mirageislands.Islands.PlayerIsland;
import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Player.PlayerDataManager;
import com.nensi.mirageislands.Utils.Gui.Default.GuiContentItem;
import com.nensi.mirageislands.Utils.Gui.Default.GuiTemplate;
import com.nensi.mirageislands.Utils.Items.ItemHandler;
import com.nensi.mirageislands.Utils.MoneyManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class IslandBuyGui extends GuiTemplate {

    private final ItemHandler itemHandler = new ItemHandler();
    private final IslandManager islandManager = new IslandManager();

    private final Island island;

    private IslandSize chosen_size = null;

    public IslandBuyGui(Player p, Island island) {

        super(p, null);

        this.island = island;

        super.id = "island_buy_gui";

        super.title = MirageIslands.utils.setColors("&6Покупка острова");
        super.size = 36;

        createInventory();

    }

    @Override
    protected List<GuiContentItem> getContents() {

        List<GuiContentItem> results = new ArrayList<>();

        results.add(new GuiContentItem(itemHandler.getItemTemplate(Material.BARRIER, 10, "&cНазад", List.of(" ", "&7Нажмите, чтобы вернуться обратно")), "back", 31));

        int slot = 11;

        for (IslandSize size : IslandSize.values()) {

            String id = island.getMainId() + "_" + size.toString().toLowerCase();

            if (!islandManager.islandExists(id)) continue;

            if (islandManager.hasIsland(p, id)) {
                results.add(new GuiContentItem(itemHandler.getItemTemplate(
                        Material.YELLOW_STAINED_GLASS_PANE,
                        10,
                        size.getName(),
                        List.of(" ", "&7Остров такого размера уже куплен")),
                        "already",
                        slot));

                slot++;
                continue;
            }

            if (chosen_size != size) {
                results.add(new GuiContentItem(itemHandler.getItemTemplate(
                        Material.LIME_STAINED_GLASS_PANE,
                        10,
                        size.getName(),
                        List.of(" ", "&7Нажмите, чтобы выбрать этот размер острова")),
                        "size:" + size,
                        slot));

            } else {
                results.add(new GuiContentItem(itemHandler.getItemTemplate(
                        Material.RED_STAINED_GLASS_PANE,
                        10,
                        size.getName(),
                        List.of(" ", "&7Вы уже выбрали этот размер")),
                        "already",
                        slot));
            }

            slot++;

        }

        if (chosen_size == null) {
            results.add(new GuiContentItem(itemHandler.getItemTemplate(Material.EMERALD, 10, "&aКупить", List.of(" ", "&7Сначала выберите размер острова!")), "buy", 15));

        } else {

            Island current_island = islandManager.getIsland(island.getMainId() + "_" + chosen_size.toString().toLowerCase());
            int cost = current_island.getCost();

            results.add(new GuiContentItem(itemHandler.getItemTemplate(
                    Material.EMERALD,
                    10,
                    "&aКупить",
                    List.of(" ", "&fЦена: &e" + cost + "$", " ", "&7Нажмите, чтобы купить")),
                    "buy",
                    15));
        }


        return results;
    }

    @Override
    public void clickButton(String id, ClickType clickType) {

        if (id.equalsIgnoreCase("back")) {
            p.closeInventory();
            MirageIslands.sounds.clientSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

            new IslandsMainGui(p).Open(0, false);
        }

        if (id.equalsIgnoreCase("buy")) {

            if (chosen_size == null) {
                p.sendMessage(MirageIslands.utils.setColors("&cСначала выберите размер острова!"));
                MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
                return;
            }

            Island current_island = islandManager.getIsland(island.getMainId() + "_" + chosen_size.toString().toLowerCase());

            if (new MoneyManager(p).countPlayerMoney() < current_island.getCost()) {
                p.sendMessage(MirageIslands.utils.setColors("&cНедостаточно средств для покупки!"));
                MirageIslands.sounds.clientSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1.0f, 1.0f);
                return;
            }

            new MoneyManager(p).takeMoney(current_island.getCost());

            p.closeInventory();
            MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            MirageIslands.sounds.clientSound(p, Sound.ENTITY_EVOKER_CELEBRATE, 1.0f, 1.0f);
            MirageIslands.sounds.clientSound(p, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.0f, 1.0f);

            p.sendMessage(MirageIslands.utils.setColors("&aОстров куплен!"));
            p.sendMessage(MirageIslands.utils.setColors("  &7- &fОстров: &7\"" + current_island.getName() + "&7\""));
            p.sendMessage(MirageIslands.utils.setColors("  &7- &fРазмер: " + current_island.getSize().getName()));

            new PlayerDataManager(p).addIsland(new PlayerIsland(IslandManager.placed_amount, p.getUniqueId().toString(), current_island));
            new IslandManager().placeIslands();

            new IslandsMainGui(p).Open(0, false);
        }

        if (id.startsWith("size")) {

            String size_id = id.split(":")[1];

            chosen_size = IslandSize.valueOf(size_id);

            p.sendMessage(MirageIslands.utils.setColors("&aРазмер выбран!"));
            MirageIslands.sounds.clientSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            Update();

        }
    }
}
