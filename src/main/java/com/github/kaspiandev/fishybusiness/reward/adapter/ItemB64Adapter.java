package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.gson.SerializingPropertyAdapter;
import com.github.kaspiandev.fishybusiness.util.InventoryUtil;
import com.google.gson.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemB64Adapter implements SerializingPropertyAdapter<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return InventoryUtil.fromBase64(json.getAsString());
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", InventoryUtil.toBase64(src));

        return jsonObject.get("item");
    }

    @Override
    public Class<ItemStack> getAdapterClass() {
        return ItemStack.class;
    }

}
