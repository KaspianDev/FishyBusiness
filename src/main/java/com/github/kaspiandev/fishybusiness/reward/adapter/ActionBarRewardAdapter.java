package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.gson.InstancePropertyAdapter;
import com.github.kaspiandev.fishybusiness.reward.ActionBarReward;

import java.lang.reflect.Type;

public class ActionBarRewardAdapter implements InstancePropertyAdapter<ActionBarReward> {

    private final FishyBusiness plugin;

    public ActionBarRewardAdapter(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public ActionBarReward createInstance(Type type) {
        return new ActionBarReward(plugin, null, null, null, 0);
    }

    @Override
    public Class<? extends ActionBarReward> getAdapterClass() {
        return ActionBarReward.class;
    }

}
