package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import org.bukkit.entity.Player;

public class PointsReward implements Reward {

    private final transient FishyBusiness plugin;
    private final String name;
    private final int amount;
    private final double weight;

    public PointsReward(FishyBusiness plugin, String name, int amount, double weight) {
        this.plugin = plugin;
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
    public void reward(Player player) {
        plugin.getPointManager().ifPresent((pointManager) -> {
            pointManager.addPoints(player, amount);
        });
    }

}
