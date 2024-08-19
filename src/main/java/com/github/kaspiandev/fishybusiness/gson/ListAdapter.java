package com.github.kaspiandev.fishybusiness.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class ListAdapter<T> implements JsonDeserializer<List<T>> {

    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<T> result = new ArrayList<>();
        if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            for (JsonElement element : array) {
                T item = context.deserialize(element, getItemClass());
                if (item != null) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    public abstract Class<T> getItemClass();

}
