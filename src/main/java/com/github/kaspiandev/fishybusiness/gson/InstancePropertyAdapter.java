package com.github.kaspiandev.fishybusiness.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public interface InstancePropertyAdapter<T> extends PropertyAdapter<T>, InstanceCreator<T> {

    @Override
    default void inject(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(this.getClass(), this);
    }

}
