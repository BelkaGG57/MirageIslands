package com.nensi.mirageislands.Islands;

import com.nensi.mirageislands.Utils.Config.CustomConfig;
import io.lumine.mythic.lib.api.player.MMOPlayerData;
import io.lumine.mythic.lib.api.stat.modifier.StatModifier;
import io.lumine.mythic.lib.player.modifier.ModifierType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicReference;

public class StatManager {

    private final Player p;
    private final MMOPlayerData data;
    private final Island island;

    public StatManager(Player p, Island island) {
        this.p = p;
        this.island = island;
        this.data = MMOPlayerData.get(p.getUniqueId());

    }

    public void Apply() {

        CustomConfig cfg = island.getConfig();

        if (cfg.getSection("stats", null).getSourceSection() == null) return;

        for (String id : cfg.getSection("stats", null).getSourceSection().getKeys(false)) {

            String key = cfg.getString("stats." + id + ".key", "none");
            String stat = cfg.getString("stats." + id + ".stat", "none");
            double value = cfg.getInteger("stats." + id + ".value", 0);

            double old_value = 0;

            if (data.getStatMap().getInstance(stat).getKeys().contains(key)) {
                old_value = data.getStatMap().getInstance(stat).getModifier(key).getValue();
            }

            StatModifier modifier = new StatModifier(key, stat, value + old_value, ModifierType.FLAT);
            modifier.register(data);



        }





    }


}
