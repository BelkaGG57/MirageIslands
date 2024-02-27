package com.nensi.mirageislands.Utils;

import com.nensi.mirageislands.MirageIslands;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUtils {

    private final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);

    public void throwEntity(Player p, Entity entity, double velocity, double y) {

        Location entity_loc = entity.getLocation();
        Location newLoc = entity_loc.subtract(p.getLocation());
        Vector newV = new Vector(newLoc.toVector().normalize().multiply(velocity).getX(), y, newLoc.toVector().normalize().multiply(velocity).getZ());
        entity.setVelocity(newV);
    }

    public void throwEntity(Location loc, Entity entity, double velocity, double y) {
        try {

            Location entity_loc = entity.getLocation();
            Location newLoc = entity_loc.subtract(loc);
            Vector newV = new Vector(newLoc.toVector().normalize().multiply(velocity).getX(), y, newLoc.toVector().normalize().multiply(velocity).getZ());
            entity.setVelocity(newV);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void throwPlayer(Player p, Entity entity, double velocity, double y) {

        Location entity_loc = p.getLocation();
        Location newLoc = entity_loc.subtract(entity.getLocation());
        Vector newV = new Vector(newLoc.toVector().normalize().multiply(velocity).getX(), y, newLoc.toVector().normalize().multiply(velocity).getZ());
        p.setVelocity(newV);
    }


    public List<Entity> getEntitiesInRadius(Location location, double radius) {

        List<Entity> entities = new ArrayList<>();
        World world = location.getWorld();

        int smallX = (int)Math.floor((location.getX() - radius) / 16.0D);
        int bigX = (int)Math.floor((location.getX() + radius) / 16.0D);
        int smallZ = (int)Math.floor((location.getZ() - radius) / 16.0D);
        int bigZ = (int)Math.floor((location.getZ() + radius) / 16.0D);

        for (int x = smallX; x <= bigX; x++) {
            for (int z = smallZ; z <= bigZ; z++) {
                assert world != null;
                if (!world.isChunkLoaded(x, z)) {
                    world.loadChunk(x, z);
                }
                entities.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities()));
            }
        }

        entities.removeIf(entity -> entity.getLocation().distanceSquared(location) > radius * radius);

        return entities;
    }

    @SuppressWarnings("unused")
    public List<Entity> getAroundLocations(List<Location> locations, double radius) {

        List<Entity> entities = new ArrayList<>();

        for (Location loc : locations) {
            entities.addAll(getEntitiesInRadius(loc, radius));
        }

        return entities;
    }

    public List<Entity> getAroundLocationsOptimised(List<Location> locations, double radius) {

        List<Entity> entities = new ArrayList<>();

        for (Location loc : locations) {
            if (loc.getWorld() == null) continue;
            entities.addAll(loc.getWorld().getNearbyEntities(loc, radius, radius, radius));
        }

        return entities;
    }

    public List<Entity> getFilterEntities(List<Entity> entities) {


        return entities.stream().filter(entity ->
                !(entity instanceof ArmorStand) &&
                        !(entity instanceof Item) &&
                        !(entity instanceof ItemFrame) &&
                        !(entity instanceof ExperienceOrb)
                ).
                collect(Collectors.toList());
    }


    public String getEntityType(Entity entity) {
        return entity.getType().toString();
    }

}
