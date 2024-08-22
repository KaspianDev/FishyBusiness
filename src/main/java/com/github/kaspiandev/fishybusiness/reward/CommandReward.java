package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.worldguard.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandReward implements Reward {

    private final String name;
    private final String command;
    private final double weight;

    public CommandReward(String name, String command, double weight) {
        this.name = name;
        this.command = command;
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
        plugin.getHookManager().findHook(PlaceholderAPIHook.class).ifPresentOrElse((hook) -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), hook.applyPlaceholders(player, command));
        }, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

}
