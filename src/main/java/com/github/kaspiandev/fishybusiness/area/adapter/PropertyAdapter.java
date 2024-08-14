package com.github.kaspiandev.fishybusiness.area.adapter;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface PropertyAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {

    Class<? extends T> getAdapterClass();

}
