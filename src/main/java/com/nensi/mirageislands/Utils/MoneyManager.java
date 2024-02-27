package com.nensi.mirageislands.Utils;

import com.nensi.mirageislands.MirageIslands;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MoneyManager {

    private final Player p;
    private final String ia_id;

    public MoneyManager(Player p) {
        this.p = p;
        this.ia_id = MirageIslands.configUtils.get("config").getString("money_item");
    }

    @SuppressWarnings("deprecation")
    public boolean isMoney(ItemStack itm){
        if (itm.getItemMeta() == null) {
            return false;
        }

        if (ItemsAdder.isCustomItem(itm)) {
            CustomStack customStack = CustomStack.byItemStack(itm);
            return customStack.getId().equalsIgnoreCase(ia_id);
        }

        return false;
    }

    public int countPlayerMoney() {
        int result = 0;
        try {
            for (ItemStack itm : p.getInventory().getContents()) {
                if (itm == null) continue;
                if (isMoney(itm)) {

                    result += itm.getAmount();

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public void takeMoney(int amount) {
        int total_cost = amount;
        try {
            for (ItemStack itm : p.getInventory().getContents()) {
                if (itm == null) continue;
                if (total_cost <= 0) return;

                if (isMoney(itm)) {

                    if (itm.getAmount() > 0) {
                        int item_amount = itm.getAmount();
                        int cost = 1;
                        int stack_cost = Math.min(total_cost, item_amount * cost);
                        itm.setAmount(itm.getAmount() - (int) Math.ceil((double) stack_cost / cost));
                        total_cost -= stack_cost;
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
