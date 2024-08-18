package com.github.kaspiandev.fishybusiness.area.adapter;

import com.github.kaspiandev.fishybusiness.area.Area;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AreaListAdapter implements JsonDeserializer<List<Area>> {

    @Override
    public List<Area> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Area> result = new ArrayList<>();
        if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            for (JsonElement element : array) {
                Area item = context.deserialize(element, Area.class);
                if (item != null) {
                    result.add(item);
                }
            }
        }
        return result;
    }
}
