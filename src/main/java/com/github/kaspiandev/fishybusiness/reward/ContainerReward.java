package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class ContainerReward implements Reward {

    private final transient FishyBusiness plugin;
    private final String name;
    private final List<String> rewardNames;
    private final double weight;

    public ContainerReward(FishyBusiness plugin, String name, List<String> rewardNames, double weight) {
        this.plugin = plugin;
        this.name = name;
        this.rewardNames = rewardNames;
        this.weight = weight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void reward(Player player) {
        rewardNames.stream()
                   .map(plugin.getRewardManager()::findReward)
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .forEach((reward) -> reward.reward(player));
    }

}
