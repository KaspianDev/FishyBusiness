package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.util.ComponentUtil;
import com.github.kaspiandev.fishybusiness.util.ItemBuilder;
import com.github.kaspiandev.fishybusiness.util.ItemLoader;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MessageReward implements Reward {

    private final String name;
    private final String message;
    private final Type messageType;
    private final double weight;

    public MessageReward(String name, String message, Type messageType, double weight) {
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
            case BROADCAST -> Bukkit.spigot().broadcast(message);
            case PLAYER -> player.spigot().sendMessage(message);
        }
    }

    @Override
    public ItemStack getDisplay(FishyBusiness plugin, Player player) {
        return plugin.getConf().getRewardDisplay(MessageReward.class)
                     .map((section) -> {
                         ItemBuilder commandBuilder = ItemLoader.loadBuilder(section);
                         String name = commandBuilder.getName();
                         if (name != null) {
                             commandBuilder.name(ComponentUtil.toString(plugin.getMessages().get(name, (oldName) -> {
                                 return oldName.replace("${name}", this.name);
                             }, player)));
                         }
                         return commandBuilder.build();
                     })
                     .orElse(new ItemStack(Material.AIR));
    }

    public enum Type {

        BROADCAST,
        PLAYER

    }

}
