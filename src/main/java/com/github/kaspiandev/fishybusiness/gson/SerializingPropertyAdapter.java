package com.github.kaspiandev.fishybusiness.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface SerializingPropertyAdapter<T> extends PropertyAdapter<T>, JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    default void inject(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeHierarchyAdapter(this.getClass(), this);
    }

}
