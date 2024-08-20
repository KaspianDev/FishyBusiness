package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageReward implements Reward {

    private final transient FishyBusiness plugin;
    private final String message;
    private final Type messageType;
    private final double weight;

    public MessageReward(FishyBusiness plugin, String message, Type messageType, double weight) {
        this.plugin = plugin;
        this.message = message;
        this.messageType = messageType;
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void reward(Player player) {
        BaseComponent[] message = plugin.getMessages().get(this.message, player);
        switch (messageType) {
            case BROADCAST -> Bukkit.spigot().broadcast(message);
            case PLAYER -> player.spigot().sendMessage(message);
        }
    }

    public enum Type {

        BROADCAST,
        PLAYER

    }

}
