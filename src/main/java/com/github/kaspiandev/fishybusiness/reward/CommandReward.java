package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.worldguard.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandReward implements Reward {

    private transient final FishyBusiness plugin;
    private final String command;
    private final double weight;

    public CommandReward(FishyBusiness plugin, String command, double weight) {
        this.plugin = plugin;
        this.command = command;
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void reward(Player player) {
        plugin.getHookManager().findHook(PlaceholderAPIHook.class).ifPresentOrElse((hook) -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), hook.applyPlaceholders(player, command));
        }, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

}
