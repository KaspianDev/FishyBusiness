package com.github.kaspiandev.fishybusiness.area;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

public class WorldGuardArea implements Area {

    private final World world;
    private final ProtectedRegion protectedRegion;

    public WorldGuardArea(World world, String id) {
        this.world = world;

        Optional<ProtectedRegion> optProtectedRegion = WorldGuardArea.find(this.world, id);
        if (optProtectedRegion.isEmpty()) {
            throw new IllegalArgumentException("Region with id " + id + " doesn't exist");
        }

        this.protectedRegion = optProtectedRegion.get();
    }

    public WorldGuardArea(World world, ProtectedRegion protectedRegion) {
        this.world = world;
        this.protectedRegion = protectedRegion;
        verifyWorld();
    }

    public static Optional<ProtectedRegion> find(String id) {
        return Bukkit.getWorlds().stream()
                     .map((world) -> find(world, id))
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .findAny();
    }

    public static Optional<ProtectedRegion> find(World world, String id) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(world));
        if (regionManager == null) return Optional.empty();

        return Optional.ofNullable(regionManager.getRegion(id));
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
