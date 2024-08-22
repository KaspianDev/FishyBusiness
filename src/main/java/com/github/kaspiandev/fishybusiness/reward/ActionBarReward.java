package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarReward implements Reward {

    private final String name;
    private final String message;
    private final Type messageType;
    private final double weight;

    public ActionBarReward(String name, String message, Type messageType, double weight) {
        this.name = name;
        this.message = message;
        this.messageType = messageType;
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
        BaseComponent[] message = plugin.getMessages().get(this.message, player);
        switch (messageType) {
            case BROADCAST -> Bukkit.getOnlinePlayers().forEach((onlinePlayer) -> {
                onlinePlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
            });
            case PLAYER -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
        }
    }

    public enum Type {

        BROADCAST,
        PLAYER

    }

}
