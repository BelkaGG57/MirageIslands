package com.nensi.mirageislands.Listener;

import com.nensi.mirageislands.Islands.IslandManager;
import com.nensi.mirageislands.MirageIslands;
import com.nensi.mirageislands.WorldHandler.WorldHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class IslandListener implements Listener {

    @EventHandler
    private void onMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        IslandManager islandManager = new IslandManager();
        if (p.getWorld().getName().equalsIgnoreCase(WorldHandler.WORLD_NAME) && !islandManager.isOnIsland(p)) {
            String[] parts = MirageIslands.configUtils.get("config").getString("return_loc", "none").split(";");
            World world = Bukkit.getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            p.teleport(new Location(world, x, y, z));
        }

    }

    @EventHandler
    private void onPlace(BlockBreakEvent e) {

        Player p = e.getPlayer();

        IslandManager islandManager = new IslandManager();
        if (islandManager.isOnIsland(p)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onBreak(BlockBreakEvent e) {

        Player p = e.getPlayer();

        IslandManager islandManager = new IslandManager();
        if (islandManager.isOnIsland(p)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        IslandManager islandManager = new IslandManager();
        if (islandManager.isOnIsland(p)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    private void onDamage(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player p) {
            IslandManager islandManager = new IslandManager();
            if (islandManager.isOnIsland(p)) {
                e.setCancelled(true);
            }
        }

    }


}
