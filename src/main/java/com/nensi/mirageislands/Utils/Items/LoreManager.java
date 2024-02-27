package com.nensi.mirageislands.Utils.Items;

import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.Utils.Config.CustomConfigSection;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class LoreManager {

    private final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);

    public List<String> getLore(ConfigurationSection section) {
        CustomConfigSection customConfigSection = new CustomConfigSection(section);
        List<String> raw_lore = customConfigSection.getStringList("lore", List.of("&7none"));
        return formatLore(raw_lore);
    }

    public List<String> getLore(CustomConfigSection fantasyConfigSection) {
        List<String> raw_lore = fantasyConfigSection.getStringList("lore", List.of("&7none"));
        return formatLore(raw_lore);
    }

    @SuppressWarnings("unused")
    public List<String> formatLore(List<String> raw_lore) {
        List<String> formatted_lore = new ArrayList<>();
        for (String line : raw_lore) {
            formatted_lore.add(plugin.utils.setColors(line));
        }
        return formatted_lore;
    }



}
