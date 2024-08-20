package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.gson.InstancePropertyAdapter;
import com.github.kaspiandev.fishybusiness.reward.CommandReward;

import java.lang.reflect.Type;

public class CommandRewardAdapter implements InstancePropertyAdapter<CommandReward> {

    private final FishyBusiness plugin;

    public CommandRewardAdapter(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandReward createInstance(Type type) {
        return new CommandReward(plugin, null, 0);
    }

    @Override
    public Class<? extends CommandReward> getAdapterClass() {
        return CommandReward.class;
    }

}
