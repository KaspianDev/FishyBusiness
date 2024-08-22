package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import org.bukkit.entity.Player;

public class PointsReward implements Reward {

    private final String name;
    private final int amount;
    private final double weight;

    public PointsReward(String name, int amount, double weight) {
        this.name = name;
        this.amount = amount;
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
    public void reward(FishyBusiness plugin, Player player) {
        plugin.getPointManager().ifPresent((pointManager) -> {
            pointManager.addPoints(player, amount);
        });
    }

}
