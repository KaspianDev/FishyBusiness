package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.gson.PropertyAdapter;

public class RewardType {

    private final Class<? extends Reward> rewardClass;
    private final PropertyAdapter<?>[] propertyAdapters;

    public RewardType(Class<? extends Reward> areaClass, PropertyAdapter<?>... propertyAdapters) {
        this.rewardClass = areaClass;
        this.propertyAdapters = propertyAdapters;
    }

    public Class<? extends Reward> getAreaClass() {
        return rewardClass;
    }

    public PropertyAdapter<?>[] getPropertyAdapters() {
        return propertyAdapters;
    }

}
