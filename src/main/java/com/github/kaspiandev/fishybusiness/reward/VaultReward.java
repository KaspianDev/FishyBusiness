package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.worldguard.VaultHook;
import org.bukkit.entity.Player;

public class VaultReward implements Reward {

    private transient final FishyBusiness plugin;
    private final double amount;
    private final double weight;

    public VaultReward(FishyBusiness plugin, double amount, double weight) {
        this.plugin = plugin;
        this.amount = amount;
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void reward(Player player) {
        plugin.getHookManager().findHook(VaultHook.class).ifPresent((hook) -> {
            hook.addMoney(player, amount);
        });
    }

}
