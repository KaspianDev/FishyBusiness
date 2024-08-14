package com.github.kaspiandev.fishybusiness.area;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class AreaManager {

    private static final Logger LOGGER = Logger.getLogger(AreaManager.class.getSimpleName());
    private static final AreaSerializer AREA_SERIALIZER = new AreaSerializer();

    static {
        AREA_SERIALIZER.register(WorldGuardArea.class);
        AREA_SERIALIZER.register(FishyArea.class);
    }

    private final FishyBusiness plugin;
    private final File areaFile;
    private List<Area> areas;

    public AreaManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.areaFile = new File(plugin.getDataFolder(), "areas.json");
        this.areas = List.of();
    }

    private void load() {
        try {
            if (areaFile.createNewFile()) {
                return;
            }
        } catch (IOException e) {
            return;
        }


        Type personListType = new TypeToken<List<Area>>() {}.getType();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(areaFile)) {
            areas = Collections.unmodifiableList(gson.fromJson(reader, new TypeToken<List<Area>>() {}.getType()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Void> save() {
        return CompletableFuture.runAsync(() -> {
            try (FileWriter writer = new FileWriter(areaFile)) {
                AREA_SERIALIZER.getGson().toJson(areas, new TypeToken<List<Area>>() {}.getType());
                Files.write(areaFile.toPath(),)
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
            }
        });
    }
}
