package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Reward {

    String getName();

    double getWeight();

    void reward(FishyBusiness plugin, Player player);

    ItemStack getDisplay(FishyBusiness plugin, Player player);

}
