package com.github.kaspiandev.fishybusiness.area;

import com.github.kaspiandev.fishybusiness.area.exception.AreaWorldMismatchException;
import org.bukkit.Location;
import org.bukkit.World;

public class FishyArea implements Area {

    private final World world;

    private final double minX;
    private final double maxX;

    private final double minY;
    private final double maxY;

    private final double minZ;
    private final double maxZ;

    public FishyArea(Location corner1, Location corner2) {
        this.world = corner1.getWorld();
        assert world != null;
        if (!world.equals(corner2.getWorld())) {
            throw new AreaWorldMismatchException();
        }
        this.minX = Math.min(corner1.getX(), corner2.getX());
        this.maxX = Math.max(corner1.getX(), corner2.getX());
        this.minY = Math.min(corner1.getY(), corner2.getY());
        this.maxY = Math.max(corner1.getY(), corner2.getY());
        this.minZ = Math.min(corner1.getZ(), corner2.getZ());
        this.maxZ = Math.max(corner1.getZ(), corner2.getZ());
    }

    public FishyArea(World world,
                     double minX, double maxX,
                     double minY, double maxY,
                     double minZ, double maxZ) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    @Override
    public boolean isInside(World world, double x, double y, double z) {
        if (!this.world.equals(world)) return false;

        return (x >= minX && x <= maxX) &&
                (y >= minY && y <= maxY) &&
                (z >= minZ && z <= maxZ);
    }

    @Override
    public Location getMinCorner() {
        return new Location(world, minX, minY, minZ);
    }

    @Override
    public Location getMaxCorner() {
        return new Location(world, maxX, maxY, maxZ);
    }

    @Override
    public double getMinX() {
        return minX;
    }

    @Override
    public double getMaxX() {
        return maxX;
    }

    @Override
    public double getMinY() {
        return minY;
    }

    @Override
    public double getMaxY() {
        return maxY;
    }

    @Override
    public double getMinZ() {
        return minZ;
    }

    @Override
    public double getMaxZ() {
        return maxZ;
    }

}
