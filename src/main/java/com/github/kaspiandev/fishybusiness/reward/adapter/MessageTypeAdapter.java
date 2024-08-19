package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.gson.SerializingPropertyAdapter;
import com.github.kaspiandev.fishybusiness.reward.MessageReward;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageTypeAdapter implements SerializingPropertyAdapter<MessageReward.Type> {

    @Override
    public MessageReward.Type deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String typeName = json.getAsJsonObject().get("message-type").getAsString();

        return MessageReward.Type.valueOf(typeName);
    }

    @Override
    public JsonElement serialize(MessageReward.Type src, Type typeOfSrc, JsonSerializationContext context) {
        System.out.println("aaaa");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message-type", src.name());

        return jsonObject;
    }

    @Override
    public Class<MessageReward.Type> getAdapterClass() {
        return MessageReward.Type.class;
    }

}
