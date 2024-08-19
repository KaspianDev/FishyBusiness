package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.gson.InstancePropertyAdapter;

import java.lang.reflect.Type;

public class FishyBusinessAdapter implements InstancePropertyAdapter<FishyBusiness> {

    private final FishyBusiness plugin;

    public FishyBusinessAdapter(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public FishyBusiness createInstance(Type type) {
        return plugin;
    }

    @Override
    public Class<? extends FishyBusiness> getAdapterClass() {
        return FishyBusiness.class;
    }

}
