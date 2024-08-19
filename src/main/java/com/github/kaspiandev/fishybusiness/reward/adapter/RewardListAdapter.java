package com.github.kaspiandev.fishybusiness.reward.adapter;

import com.github.kaspiandev.fishybusiness.gson.ListAdapter;
import com.github.kaspiandev.fishybusiness.reward.Reward;

public class RewardListAdapter extends ListAdapter<Reward> {

    @Override
    public Class<Reward> getItemClass() {
        return Reward.class;
    }

}
