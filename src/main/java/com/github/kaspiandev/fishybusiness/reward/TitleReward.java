package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TitleReward implements Reward {

    private final transient FishyBusiness plugin;
    private final String name;
    private final String title;
    private final String subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;
    private final Type messageType;
    private final double weight;

    public TitleReward(FishyBusiness plugin, String name, String title, String subtitle, Type messageType, double weight) {
        this.plugin = plugin;
        this.name = name;
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = 10;
        this.stay = 70;
        this.fadeOut = 20;
        this.messageType = messageType;
        this.weight = weight;
    }

    public TitleReward(FishyBusiness plugin, String name, String title, String subtitle,
                       int fadeIn, int stay, int fadeOut, Type messageType, double weight) {
        this.plugin = plugin;
        this.name = name;
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
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
    public void reward(Player player) {
        String title = (this.title == null)
                ? null
                : ComponentUtil.toString(plugin.getMessages().get(this.title, player));
        String subtitle = (this.subtitle == null)
                ? null
                : ComponentUtil.toString(plugin.getMessages().get(this.subtitle, player));

        switch (messageType) {
            case BROADCAST -> Bukkit.getOnlinePlayers().forEach((onlinePlayer) -> {
                onlinePlayer.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            });
            case PLAYER -> player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public enum Type {

        BROADCAST,
        PLAYER

    }

}
