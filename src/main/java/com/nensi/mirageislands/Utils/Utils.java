package com.nensi.mirageislands.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public final char COLOR_CHAR = ChatColor.COLOR_CHAR;

    public String translateHexColorCodes(String startTag, String endTag, String message)
    {
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    public String translateGradientColorCodes(String startTag, String endTag, String message)
    {
        final Pattern colorsPattern = Pattern.compile(startTag + "(.*?)" + endTag);
        Matcher matcher = colorsPattern.matcher(message);
        while (matcher.find())
        {
            String gradientString = matcher.group(1);

            String[] gradientSplit = gradientString.split(",");

            if (gradientSplit.length != 2) continue;

            final Color startColor = hex2Rgb(gradientSplit[0]);
            final Color endColor = hex2Rgb(gradientSplit[1]);

            final Pattern gradientPattern = Pattern.compile(startTag + gradientString + endTag + "(.*?)" + "</gradient>");
            Matcher gradientMatcher = gradientPattern.matcher(message);

            while (gradientMatcher.find())
            {
                String part = gradientMatcher.group(1);
                String formattedPart = hsvGradient(part, startColor, endColor, new LinearInterpolator());
                message = message.replaceAll(startTag + gradientString + endTag + part + "</gradient>", formattedPart);

            }

        }
        return message;
    }

    public String setColors(String s) {
        return ChatColor.translateAlternateColorCodes('&',
                translateHexColorCodes("<#", ">",
                        translateGradientColorCodes("<gradient:", ">", s)));
    }

    public Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ));
    }

    @SuppressWarnings("deprecation")
    public String hsvGradient(String str, Color from, Color to, LinearInterpolator interpolator) {

        final float[] hsvFrom = Color.RGBtoHSB(from.getRed(), from.getGreen(), from.getBlue(), null);
        final float[] hsvTo = Color.RGBtoHSB(to.getRed(), to.getGreen(), to.getBlue(), null);

        final double[] h = interpolator.interpolate(hsvFrom[0], hsvTo[0], str.length());
        final double[] s = interpolator.interpolate(hsvFrom[1], hsvTo[1], str.length());
        final double[] v = interpolator.interpolate(hsvFrom[2], hsvTo[2], str.length());

        final StringBuilder builder = new StringBuilder();

        for (int i = 0 ; i < str.length(); i++) {
            builder.append(net.md_5.bungee.api.ChatColor.of(Color.getHSBColor((float) h[i], (float) s[i], (float) v[i]))).append(str.charAt(i));
        }
        return builder.toString();
    }

    public List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }


    @SuppressWarnings("unused")
    public String getNumberFormat(int number, int size) {
        String s_n = String.valueOf(number);
        int empty_part = size - s_n.length();
        if (empty_part > 0) {
            return setColors("&8" + "0".repeat(empty_part) + "&e" + s_n);
        }
        return setColors("&e" + s_n);
    }




}
