package com.github.kaspiandev.fishybusiness.area.adapter;

import com.github.kaspiandev.fishybusiness.gson.SerializingPropertyAdapter;
import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.UUID;

public class WorldAdapter implements SerializingPropertyAdapter<World> {

    @Override
    public World deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String worldUid = json.getAsJsonObject().get("world-uid").getAsString();

        return Bukkit.getWorld(UUID.fromString(worldUid));
    }

    @Override
    public JsonElement serialize(World src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("world-uid", src.getUID().toString());

        return jsonObject;
    }

    @Override
    public Class<World> getAdapterClass() {
        return World.class;
    }

}
