package com.github.kaspiandev.fishybusiness.gson;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface SerializingPropertyAdapter<T> extends PropertyAdapter<T>, JsonSerializer<T>, JsonDeserializer<T> {

}
