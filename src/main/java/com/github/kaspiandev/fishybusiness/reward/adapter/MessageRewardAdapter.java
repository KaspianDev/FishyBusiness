package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.gson.InstancePropertyAdapter;
import com.github.kaspiandev.fishybusiness.reward.MessageReward;

import java.lang.reflect.Type;

public class MessageRewardAdapter implements InstancePropertyAdapter<MessageReward> {

    private final FishyBusiness plugin;

    public MessageRewardAdapter(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @Override
    public MessageReward createInstance(Type type) {
        return new MessageReward(plugin, null, null, 0);
    }

    @Override
    public Class<? extends MessageReward> getAdapterClass() {
        return MessageReward.class;
    }

}
