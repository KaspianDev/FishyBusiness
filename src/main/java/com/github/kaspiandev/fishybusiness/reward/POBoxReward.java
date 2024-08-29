package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.POBoxHook;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class POBoxReward implements Reward {

    private final String name;
    private final String rewardName;
    private final double weight;

    public POBoxReward(String name, String rewardName, double weight) {
        this.name = name;
        this.rewardName = rewardName;
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
        plugin.getHookManager().findHook(POBoxHook.class).ifPresent((hook) -> {
            hook.sendMail(player, hook.buildRewardMail(name, rewardName));
        });
    }

    @Override
    public ItemStack getDisplay(FishyBusiness plugin, Player player) {
        return plugin.getRewardManager().findReward(rewardName)
                     .map((reward) -> reward.getDisplay(plugin, player))
                     .orElse(new ItemStack(Material.AIR));
    }

}
