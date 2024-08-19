package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.gson.PropertyAdapter;
import com.github.kaspiandev.fishybusiness.reward.Reward;
import com.github.kaspiandev.fishybusiness.reward.RewardType;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardAdapter implements JsonDeserializer<Reward>, JsonSerializer<Reward> {

    private final Map<String, Class<? extends Reward>> rewardRegistry;
    private final List<PropertyAdapter<?>> registeredAdapters;
    private final GsonBuilder gsonBuilder;
    private Gson gson;

    public RewardAdapter() {
        this.gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
        this.rewardRegistry = new HashMap<>();
        this.registeredAdapters = new ArrayList<>();
    }

    public void register(RewardType rewardType) {
        for (PropertyAdapter<?> adapter : rewardType.getPropertyAdapters()) {
            if (registeredAdapters.contains(adapter)) continue;
            adapter.inject(gsonBuilder);
            registeredAdapters.add(adapter);
        }
        rewardRegistry.put(rewardType.getAreaClass().getSimpleName(), rewardType.getAreaClass());
        rebuildGson();
    }

    private void rebuildGson() {
        gson = gsonBuilder.create();
    }

    @Override
    public Reward deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject areaObject = json.getAsJsonObject();
        JsonElement typeElement = areaObject.get("type");

        Class<? extends Reward> rewardType = rewardRegistry.get(typeElement.getAsString());
        if (rewardType == null) return null;

        return gson.fromJson(areaObject, rewardType);
    }

    @Override
    public JsonElement serialize(Reward src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject areaObject = gson.toJsonTree(src).getAsJsonObject();
        areaObject.addProperty("type", src.getClass().getSimpleName());

        return areaObject;
    }

}
