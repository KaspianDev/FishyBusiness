package com.github.kaspiandev.fishybusiness.reward;

import org.bukkit.entity.Player;

import java.util.List;

public class ContainerReward implements Reward {

    private final String name;
    private final List<Reward> rewards;
    private final double weight;

    public ContainerReward(String name, List<Reward> rewards, double weight) {
        this.name = name;
        this.rewards = rewards;
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
        rewards.forEach((reward) -> reward.reward(player));
    }

}
