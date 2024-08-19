package com.github.kaspiandev.fishybusiness.area;

import com.github.kaspiandev.fishybusiness.gson.PropertyAdapter;

public class AreaType {

    private final Class<? extends Area> areaClass;
    private final PropertyAdapter<?>[] propertyAdapters;

    public AreaType(Class<? extends Area> areaClass, PropertyAdapter<?>... propertyAdapters) {
        this.areaClass = areaClass;
        this.propertyAdapters = propertyAdapters;
    }

    public Class<? extends Area> getAreaClass() {
        return areaClass;
    }

    public PropertyAdapter<?>[] getPropertyAdapters() {
        return propertyAdapters;
    }

}
