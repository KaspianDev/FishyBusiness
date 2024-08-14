package com.github.kaspiandev.fishybusiness.area;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldGuardArea implements Area {

    private final ProtectedRegion protectedRegion;

    public WorldGuardArea(ProtectedRegion protectedRegion) {
        this.protectedRegion = protectedRegion;
    }

    @Override
    public boolean isInside(World world, double x, double y, double z) {
        // World checking
        return protectedRegion.contains((int) x, (int) y, (int) z);
    }

    @Override
    public Location getMinCorner() {
        return BukkitAdapter.adapt(protectedRegion.getw, protectedRegion.getMinimumPoint())
    }

    @Override
    public Location getMaxCorner() {
        return null;
    }

}
