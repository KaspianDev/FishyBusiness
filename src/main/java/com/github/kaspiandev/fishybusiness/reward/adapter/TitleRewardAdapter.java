package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.gson.InstancePropertyAdapter;
import com.github.kaspiandev.fishybusiness.reward.TitleReward;

import java.lang.reflect.Type;

public class TitleRewardAdapter implements InstancePropertyAdapter<TitleReward> {

    private final FishyBusiness plugin;

    public TitleRewardAdapter(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public TitleReward createInstance(Type type) {
        return new TitleReward(plugin, null, null, null, null, 0);
    }

    @Override
    public Class<? extends TitleReward> getAdapterClass() {
        return TitleReward.class;
    }

}
