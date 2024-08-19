package com.github.kaspiandev.fishybusiness.gson;

public interface PropertyAdapter<T> {

    Class<? extends T> getAdapterClass();

}
