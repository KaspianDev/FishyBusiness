package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.gson.InstancePropertyAdapter;
import com.github.kaspiandev.fishybusiness.reward.VaultReward;

import java.lang.reflect.Type;

public class VaultRewardAdapter implements InstancePropertyAdapter<VaultReward> {

    private final FishyBusiness plugin;

    public VaultRewardAdapter(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public VaultReward createInstance(Type type) {
        return new VaultReward(plugin, null, 0, 0);
    }

    @Override
    public Class<? extends VaultReward> getAdapterClass() {
        return VaultReward.class;
    }

}
