package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.util.ComponentUtil;
import com.github.kaspiandev.fishybusiness.util.ItemBuilder;
import com.github.kaspiandev.fishybusiness.util.ItemLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TitleReward implements Reward {

    private final String name;
    private final String title;
    private final String subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;
    private final Type messageType;
    private final double weight;

    public TitleReward(String name, String title, String subtitle, Type messageType, double weight) {
        this.name = name;
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = 10;
        this.stay = 70;
        this.fadeOut = 20;
        this.messageType = messageType;
        this.weight = weight;
    }

    public TitleReward(String name, String title, String subtitle,
                       int fadeIn, int stay, int fadeOut, Type messageType, double weight) {
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
    public void reward(FishyBusiness plugin, Player player) {
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

    @Override
    public ItemStack getDisplay(FishyBusiness plugin, Player player) {
        return plugin.getConf().getRewardDisplay(TitleReward.class)
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
