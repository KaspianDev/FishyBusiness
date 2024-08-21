package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.gson.InstancePropertyAdapter;
import com.github.kaspiandev.fishybusiness.reward.PointsReward;

import java.lang.reflect.Type;

public class PointsRewardAdapter implements InstancePropertyAdapter<PointsReward> {

    private final FishyBusiness plugin;

    public PointsRewardAdapter(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public PointsReward createInstance(Type type) {
        return new PointsReward(plugin, null, 0, 0);
    }

    @Override
    public Class<? extends PointsReward> getAdapterClass() {
        return PointsReward.class;
    }

}
