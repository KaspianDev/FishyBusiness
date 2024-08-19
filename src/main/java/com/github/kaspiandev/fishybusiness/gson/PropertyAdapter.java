package com.github.kaspiandev.fishybusiness.gson;

import com.google.gson.GsonBuilder;

public interface PropertyAdapter<T> {

    Class<? extends T> getAdapterClass();

    void inject(GsonBuilder gsonBuilder);

}
