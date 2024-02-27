package com.nensi.mirageislands.Utils.Location;

import com.nensi.mirageislands.Utils.LinearInterpolator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class LocationTweaks {


    public Vector getForwardHeadDirection(Location loc, double length) {
        return loc.getDirection().normalize().multiply(length);
    }

    public Vector getBackwardHeadDirection(Location loc, double length) {
        Vector direction = loc.getDirection().normalize();
        return new Vector(-direction.getX(), 0.0, -direction.getZ()).normalize().multiply(length);
    }

    public Vector getRightHeadDirection(Location loc, double length) {
        Vector direction = loc.getDirection().normalize();
        return new Vector(-direction.getZ(), 0.0, direction.getX()).normalize().multiply(length);
    }

    public Vector getLeftHeadDirection(Location loc, double length) {
        Vector direction = loc.getDirection().normalize();
        return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize().multiply(length);
    }


    public Location add(Location loc, double f, double y, double side) {
        return loc.add(getForwardHeadDirection(loc, f)).add(getRightHeadDirection(loc, side)).add(0, y, 0);
    }

    public List<Location> getLineLocations(Location from, Location to, int points) {

        List<Location> results = new ArrayList<>();

        World world = from.getWorld();

        LinearInterpolator linearInterpolator = new LinearInterpolator();
        double[] x = linearInterpolator.interpolate(from.getX(), to.getX(), points);
        double[] y = linearInterpolator.interpolate(from.getY(), to.getY(), points);
        double[] z = linearInterpolator.interpolate(from.getZ(), to.getZ(), points);

        for (int i = 0; i < points; i++) {
            results.add(new Location(world, x[i], y[i], z[i]));
        }

        return results;
    }


    public Vector rotateVectorAroundY(Vector vector, double degrees) {
        double rad = Math.toRadians(degrees);

        double currentX = vector.getX();
        double currentZ = vector.getZ();

        double cosine = Math.cos(rad);
        double sine = Math.sin(rad);

        return new Vector((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
    }


    public static Vector getVectorFromLocToLoc(Location loc1, Location loc2) {
        return loc2.toVector().subtract(loc1.toVector());
    }


    public static double compareVectors(Vector vec1, Vector vec2) {

        double dotProduct = vec1.normalize().dot(vec2.normalize());

        double similarity = (dotProduct + 1) / 2;

        return similarity * 100;
    }


}
