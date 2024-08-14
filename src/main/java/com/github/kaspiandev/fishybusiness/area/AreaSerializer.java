package com.github.kaspiandev.fishybusiness.area;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AreaSerializer implements JsonDeserializer<Area>, JsonSerializer<Area> {

    private final Map<String, Class<? extends Area>> areaRegistry;
    private final Gson gson;

    public AreaSerializer() {
        this.gson = new Gson();
        this.areaRegistry = new HashMap<>();
    }

    public void register(Class<? extends Area> areaClazz) {
        areaRegistry.put(areaClazz.getSimpleName(), areaClazz);
    }

    @Override
    public Area deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject areaObject = json.getAsJsonObject();
        JsonElement typeElement = areaObject.get("type");

        Class<? extends Area> areaType = areaRegistry.get(typeElement.getAsString());
        return gson.fromJson(areaObject, areaType);
    }

    @Override
    public JsonElement serialize(Area src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject areaObject = gson.toJsonTree(src).getAsJsonObject();
        areaObject.addProperty("type", src.getClass().getSimpleName());

        return areaObject;
    }

    public Gson getGson() {
        return gson;
    }

}
