package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.gson.InstancePropertyAdapter;
import com.github.kaspiandev.fishybusiness.reward.ContainerReward;

import java.lang.reflect.Type;

public class ContainerRewardAdapter implements InstancePropertyAdapter<ContainerReward> {

    private final FishyBusiness plugin;

    public ContainerRewardAdapter(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public ContainerReward createInstance(Type type) {
        return new ContainerReward(plugin, null, null, 0);
    }

    @Override
    public Class<? extends ContainerReward> getAdapterClass() {
        return ContainerReward.class;
    }

}
