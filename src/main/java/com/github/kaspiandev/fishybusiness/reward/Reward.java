package com.github.kaspiandev.fishybusiness.reward;

import org.bukkit.entity.Player;

public interface Reward {

    String getName();

    double getWeight();

    void reward(Player player);

}
