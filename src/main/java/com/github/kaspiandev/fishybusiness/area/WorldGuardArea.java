package com.github.kaspiandev.fishybusiness.area;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.World;

public class WorldGuardArea implements Area {

    private final World world;
    private final ProtectedRegion protectedRegion;

    public WorldGuardArea(World world, ProtectedRegion protectedRegion) {
        this.world = world;
        this.protectedRegion = protectedRegion;
        verifyWorld();
    }

    @Override
    public boolean isInside(World world, double x, double y, double z) {
        if (!this.world.equals(world)) return false;

        return protectedRegion.contains((int) x, (int) y, (int) z);
    }

    @Override
    public Location getMinCorner() {
        return BukkitAdapter.adapt(world, protectedRegion.getMinimumPoint());
    }

    @Override
    public Location getMaxCorner() {
        return BukkitAdapter.adapt(world, protectedRegion.getMaximumPoint());
    }

    private void verifyWorld() {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(world));
        if (regionManager == null || !regionManager.hasRegion(protectedRegion.getId())) {
            throw new IllegalStateException("World doesn't match the ProtectedRegion's world");
        }
    }

}
