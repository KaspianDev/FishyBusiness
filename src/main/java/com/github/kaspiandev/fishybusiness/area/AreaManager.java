package com.github.kaspiandev.fishybusiness.area;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.area.adapter.AreaListAdapter;
import com.github.kaspiandev.fishybusiness.area.exception.AreaOverlapException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Logger;

public class AreaManager {

    private static final Logger LOGGER = Logger.getLogger(AreaManager.class.getSimpleName());
    private static final Type AREA_LIST_TYPE = new TypeToken<List<Area>>() {}.getType();

    private final Gson gson;
    private final FishyBusiness plugin;
    private final File areaFile;
    private final List<Area> areas;
    private final Map<UUID, Area> playerAreas;

    public AreaManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Area.class, plugin.getAreaAdapter())
                .registerTypeAdapter(AREA_LIST_TYPE, new AreaListAdapter())
                .create();
        this.areaFile = new File(plugin.getDataFolder(), "areas.json");
        this.areas = new ArrayList<>();
        this.playerAreas = new HashMap<>();
        load();
    }

    public void addArea(Area area) {
        Optional<Area> minCornerArea = findArea(area.getMinCorner());
        if (minCornerArea.isPresent()) {
            throw new AreaOverlapException();
        }

        Optional<Area> maxCornerArea = findArea(area.getMaxCorner());
        if (maxCornerArea.isPresent()) {
            throw new AreaOverlapException();
        }

        areas.add(area);
        save();
    }

    public void removeArea(Area area) {
        areas.remove(area);
        save();
    }

    public Optional<Area> findArea(Location location) {
        return areas.stream()
                    .filter((area) -> area.isInside(location))
                    .findFirst();
    }

    private void load() {
        try {
            if (areaFile.createNewFile()) return;
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            return;
        }

        try (FileReader reader = new FileReader(areaFile)) {
            List<Area> loadedAreas = gson.fromJson(reader, AREA_LIST_TYPE);
            if (loadedAreas != null) areas.addAll(loadedAreas);
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(areaFile)) {
            writer.write(gson.toJson(areas, AREA_LIST_TYPE));
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    public void putPlayerArea(Player player, Area area) {
        playerAreas.put(player.getUniqueId(), area);
    }

    public Optional<Area> getPlayerArea(Player player) {
        return Optional.ofNullable(playerAreas.get(player.getUniqueId()));
    }

    public Map<UUID, Area> getPlayerAreas() {
        return playerAreas;
    }

    public void clearPlayerArea(Player player) {
        playerAreas.remove(player.getUniqueId());
    }

}
