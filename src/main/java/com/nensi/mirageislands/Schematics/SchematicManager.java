package com.nensi.mirageislands.Schematics;

import com.nensi.mirageislands.MirageIslands;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SchematicManager {

    private final MirageIslands plugin = MirageIslands.getPlugin(MirageIslands.class);

    public Clipboard getSchematic(String id) {
        try {

            File file = new File(plugin.getDataFolder().getAbsolutePath() + "/Schematics/" + id + ".schem");
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            assert format != null;
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            return reader.read();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @SuppressWarnings("deprecation")
    public void placeSchematic(Location loc, String id){

        loc.getBlock().setType(Material.AIR);;

        try {

            Clipboard clipboard = getSchematic(id);
            assert clipboard != null;

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), -1)) {

                ClipboardHolder holder = new ClipboardHolder(clipboard);
                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
                        .ignoreAirBlocks(true)
                        .build();

                Operations.complete(operation);

            }

        } catch (WorldEditException e) {
            e.printStackTrace();

        }
    }


    public void removeSchematic(Location loc, String id) {

        Clipboard clipboard = getSchematic(id);

        assert clipboard != null;
        BlockVector3 minPoint = clipboard.getMinimumPoint();
        BlockVector3 maxPoint = clipboard.getMaximumPoint();
        BlockVector3 origin = clipboard.getOrigin();

        for (int x = minPoint.getBlockX(); x < maxPoint.getBlockX() + 1; x++) {
            for (int y = minPoint.getBlockY(); y < maxPoint.getBlockY() + 1; y++) {
                for (int z = minPoint.getBlockZ(); z < maxPoint.getBlockZ() + 1; z++) {

                    int delta_x = -(origin.getBlockX() - x);
                    int delta_y = -(origin.getBlockY() - y);
                    int delta_z = -(origin.getBlockZ() - z);

                    Location pos = loc.clone();
                    pos = pos.add(delta_x, delta_y, delta_z);

                    pos.getBlock().setType(Material.AIR);


                }
            }
        }


    }

}
