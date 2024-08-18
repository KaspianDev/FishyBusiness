package com.github.kaspiandev.fishybusiness.pdc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class LocationPersistentDataType implements PersistentDataType<String, Location> {

    public static final LocationPersistentDataType INSTANCE = new LocationPersistentDataType();
    public static final Location INVALID_LOCATION = new Location(null, 0, 0, 0);

    private LocationPersistentDataType() {}

    @NotNull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @NotNull
    @Override
    public Class<Location> getComplexType() {
        return Location.class;
    }

    @NotNull
    @Override
    public String toPrimitive(@NotNull Location complex, @NotNull PersistentDataAdapterContext context) {
        World world = complex.getWorld();
        if (world == null) return "";

        return world.getName() + ":" +
                complex.getX() + ":" +
                complex.getY() + ":" +
                complex.getZ();
    }

    @NotNull
    @Override
    public Location fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        if (primitive.isEmpty()) return INVALID_LOCATION;

        String[] pieces = primitive.split(":");
        World world = Bukkit.getWorld(pieces[0]);
        double x = Double.parseDouble(pieces[1]);
        double y = Double.parseDouble(pieces[2]);
        double z = Double.parseDouble(pieces[3]);

        return new Location(world, x, y, z);
    }

}
