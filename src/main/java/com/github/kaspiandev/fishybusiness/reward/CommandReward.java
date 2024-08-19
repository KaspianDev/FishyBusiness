package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.worldguard.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandReward implements Reward {

    private transient final FishyBusiness plugin;
    private final String command;

    public CommandReward(FishyBusiness plugin, String command) {
        this.plugin = plugin;
        this.command = command;
    }

    @Override
    public void reward(Player player) {
        plugin.getHookManager().findHook(PlaceholderAPIHook.class).ifPresentOrElse((hook) -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), hook.applyPlaceholders(player, command));
        }, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

}
