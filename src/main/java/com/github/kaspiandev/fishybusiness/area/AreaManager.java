package com.github.kaspiandev.fishybusiness.area;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.area.adapter.AreaAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AreaManager {

    private static final Logger LOGGER = Logger.getLogger(AreaManager.class.getSimpleName());
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Area.class, new AreaAdapter())
                .create();
    }

    private final AreaAdapter areaAdapter;
    private final FishyBusiness plugin;
    private final File areaFile;
    private final List<Area> areas;

    public AreaManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.areaAdapter = new AreaAdapter();
        this.areaFile = new File(plugin.getDataFolder(), "areas.json");
        this.areas = new ArrayList<>();
        load();
        addArea(new FishyArea(Bukkit.getWorld("world"), 10, 20, 30, 20, 30, 40));
        System.out.println(areas);
        save();
    }

    public void addArea(Area area) {
        areas.add(area);
    }

    private void load() {
        try {
            if (areaFile.createNewFile()) return;
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            return;
        }

        try (FileReader reader = new FileReader(areaFile)) {
            List<Area> loadedAreas = GSON.fromJson(reader, new TypeToken<List<Area>>() {}.getType());
            if (loadedAreas != null) areas.addAll(loadedAreas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {

        try (FileWriter writer = new FileWriter(areaFile)) {
            writer.write(GSON.toJson(areas, new TypeToken<List<Area>>() {}.getType()));
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }

    }
}
