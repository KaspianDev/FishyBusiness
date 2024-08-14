package com.github.kaspiandev.fishybusiness.area;

import org.bukkit.Location;
import org.bukkit.World;

public interface Area {

    boolean isInside(World world, double x, double y, double z);

    default boolean isInside(Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        return isInside(world, location.getX(), location.getY(), location.getZ());
    }

    Location getMinCorner();

    Location getMaxCorner();

}
