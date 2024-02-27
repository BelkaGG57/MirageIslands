package com.nensi.mirageislands.Utils.Sounds;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.nensi.mirageislands.Utils.EntityUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SoundUtils {

    Random random = new Random();
    EntityUtils entityUtils = new EntityUtils();


    public SoundUtils() {
    }


    public void parseSounds(List<String> sounds, Player p) {
        for (String sound : sounds) {

            String[] options = sound.split(";");

            if (!options[0].equalsIgnoreCase("radius")) {

                Sound s_type = Sound.valueOf(options[0]);
                float s_volume =  Float.parseFloat(options[1]);
                float s_pitch = Float.parseFloat(options[2]);
                clientSound(p, s_type, s_volume, s_pitch);

            } else {

                Sound s_type = Sound.valueOf(options[1]);
                double s_radius = Double.parseDouble(options[2]);
                float s_pitch = Float.parseFloat(options[3]);
                float s_volume = Float.parseFloat(options[4]);
                clientSound(p.getLocation(), s_radius, s_type, s_volume, s_pitch);

            }

        }
    }
    public void parseSounds(List<String> sounds, Location loc) {
        for (String sound : sounds) {
            String[] options = sound.split(";");

            if (!options[0].equalsIgnoreCase("custom")) {

                Sound s_type = Sound.valueOf(options[0]);
                double s_radius = Double.parseDouble(options[1]);

                float s_pitch = Float.parseFloat(options[2]);
                float s_volume = Float.parseFloat(options[3]);
                clientSound(loc, s_radius, s_type, s_volume, s_pitch);

            } else {

                String s_type = options[1];


                if (s_type.contains("-")) {

                    String[] sound_type_op = s_type.split("\\.");

                    String[] numbers = sound_type_op[Math.max(0, sound_type_op.length-1)].split("-");
                    int[] range = {Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1])};

                    int value = random.nextInt(range[1] - range[0] + 1) + range[0];


                    StringBuilder builder = new StringBuilder();
                    builder.append(sound_type_op[0]);
                    for (int i = 1; i < Math.max(0, sound_type_op.length-1); i++) {
                        builder.append(".").append(sound_type_op[i]);
                    }
                    builder.append(".").append(value);

                    s_type = builder.toString();

                }

                float s_pitch = Float.parseFloat(options[3]);
                float s_volume =  Float.parseFloat(options[2]);

                Objects.requireNonNull(loc.getWorld()).playSound(loc, s_type, s_volume, s_pitch);
            }


        }
    }

    public void customSound(Player p, String sound, float vol, float pitch) {

        p.getWorld().playSound(p.getLocation(), sound, vol, pitch);

    }

    public void clientSound(Player p, Sound sound, float vol, float pitch) {
        Location loc = p.getLocation();
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        PacketContainer packet =pm.createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT);
        packet.getModifier().writeDefaults();
        packet.getSoundEffects().write(0, sound);
        packet.getSoundCategories().write(0, EnumWrappers.SoundCategory.MASTER);
        packet.getIntegers().write(0, loc.getBlockX()*8).write(1, loc.getBlockY()*8).write(2, loc.getBlockZ()*8);
        packet.getFloat().write(0, vol).write(1, pitch);
        try {
            pm.sendServerPacket(p, packet);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }

    }

    public void clientSound(Location loc, double radius, Sound sound, float vol, float pitch) {

        Collection<Entity> players = entityUtils.getEntitiesInRadius(loc, radius).stream().filter(entity -> entity instanceof Player).toList();

        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        PacketContainer packet =pm.createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT);
        packet.getModifier().writeDefaults();
        packet.getSoundEffects().write(0, sound);
        packet.getSoundCategories().write(0, EnumWrappers.SoundCategory.MASTER);
        packet.getIntegers().
                write(0, loc.getBlockX() * 8).
                write(1, loc.getBlockY() * 8).
                write(2, loc.getBlockZ() * 8);
        packet.getFloat().write(0, vol).write(1, pitch);
        try {

            if (players.size() > 0) {
                for (Entity entity : players) {
                    Player p = (Player) entity;
                    Location l = p.getLocation();

                    packet.getIntegers().
                            write(0, l.getBlockX() * 8).
                            write(1, l.getBlockY() * 8).
                            write(2, l.getBlockZ() * 8);

                    pm.sendServerPacket(p, packet);
                }
            }
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void clientSounds(Location loc, double radius, List<String> sounds) {
        for (String s : sounds) {
            String[] options = s.split(":");
            Sound sound = Sound.valueOf(options[0]);
            int vol = Integer.parseInt(options[1]);
            float pitch = (float)Double.parseDouble(options[2]);
            clientSound(loc, radius, sound, vol, pitch);
        }
    }

}
