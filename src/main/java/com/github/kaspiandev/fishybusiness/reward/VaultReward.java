package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.worldguard.VaultHook;
import org.bukkit.entity.Player;

public class VaultReward implements Reward {

    private final transient FishyBusiness plugin;
    private final String name;
    private final double amount;
    private final double weight;

    public VaultReward(FishyBusiness plugin, String name, double amount, double weight) {
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
        plugin.getHookManager().findHook(VaultHook.class).ifPresent((hook) -> {
            hook.addMoney(player, amount);
        });
    }

}
