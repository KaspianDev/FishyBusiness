package com.github.kaspiandev.fishybusiness.area;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.area.exception.AreaOverlapException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class AreaManager {

    private static final Logger LOGGER = Logger.getLogger(AreaManager.class.getSimpleName());

    private final Gson gson;
    private final FishyBusiness plugin;
    private final File areaFile;
    private final List<Area> areas;

    public AreaManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Area.class, plugin.getAreaAdapter())
                .create();
        this.areaFile = new File(plugin.getDataFolder(), "areas.json");
        this.areas = new ArrayList<>();
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
            List<Area> loadedAreas = gson.fromJson(reader, new TypeToken<List<Area>>() {}.getType());
            if (loadedAreas != null) areas.addAll(loadedAreas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(areaFile)) {
            writer.write(gson.toJson(areas, new TypeToken<List<Area>>() {}.getType()));
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }

    }

}
