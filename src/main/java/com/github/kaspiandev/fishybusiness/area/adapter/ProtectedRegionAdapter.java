package com.github.kaspiandev.fishybusiness.area.adapter;

import com.github.kaspiandev.fishybusiness.area.WorldGuardArea;
import com.google.gson.*;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import java.lang.reflect.Type;
import java.util.Optional;

public class ProtectedRegionAdapter implements PropertyAdapter<ProtectedRegion> {

    @Override
    public ProtectedRegion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        Optional<ProtectedRegion> protectedRegion = WorldGuardArea.find(object.get("id").getAsString());
        if (protectedRegion.isEmpty()) {
            throw new JsonParseException("Region does not exist.");
        }

        return protectedRegion.get();
    }

    @Override
    public JsonElement serialize(ProtectedRegion src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.getId());

        return jsonObject;
    }

    @Override
    public Class<ProtectedRegion> getAdapterClass() {
        return ProtectedRegion.class;
    }

}
