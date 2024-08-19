package com.github.kaspiandev.fishybusiness.area.adapter;

import com.github.kaspiandev.fishybusiness.area.Area;
import com.github.kaspiandev.fishybusiness.area.AreaType;
import com.github.kaspiandev.fishybusiness.gson.PropertyAdapter;
import com.google.gson.*;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AreaAdapter implements JsonDeserializer<Area>, JsonSerializer<Area> {

    private final Map<String, Class<? extends Area>> areaRegistry;
    private final GsonBuilder gsonBuilder;
    private Gson gson;

    public AreaAdapter() {
        this.gsonBuilder = new GsonBuilder()
                .registerTypeHierarchyAdapter(World.class, new WorldAdapter());
        this.gson = gsonBuilder.create();
        this.areaRegistry = new HashMap<>();
    }

    public void register(AreaType areaType) {
        for (PropertyAdapter<?> adapter : areaType.getPropertyAdapters()) {
            gsonBuilder.registerTypeHierarchyAdapter(adapter.getAdapterClass(), adapter);
        }
        areaRegistry.put(areaType.getAreaClass().getSimpleName(), areaType.getAreaClass());
        rebuildGson();
    }

    private void rebuildGson() {
        gson = gsonBuilder.create();
    }

    @Override
    public Area deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject areaObject = json.getAsJsonObject();
        JsonElement typeElement = areaObject.get("type");

        Class<? extends Area> areaType = areaRegistry.get(typeElement.getAsString());
        if (areaType == null) return null;

        return gson.fromJson(areaObject, areaType);
    }

    @Override
    public JsonElement serialize(Area src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject areaObject = gson.toJsonTree(src).getAsJsonObject();
        areaObject.addProperty("type", src.getClass().getSimpleName());

        return areaObject;
    }

}
