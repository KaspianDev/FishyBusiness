package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.util.ComponentUtil;
import com.github.kaspiandev.fishybusiness.util.ItemBuilder;
import com.github.kaspiandev.fishybusiness.util.ItemLoader;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SoundReward implements Reward {

    private final String name;
    private final Sound sound;
    private final float volume;
    private final float pitch;
    private final double weight;

    public SoundReward(String name, Sound sound, float volume, float pitch, double weight) {
        this.name = name;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.weight = weight;
    }

    public SoundReward(String name, Sound sound, double weight) {
        this.name = name;
        this.sound = sound;
        this.volume = 1;
        this.pitch = 1;
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
        player.playSound(player, sound, volume, pitch);
    }

    @Override
    public ItemStack getDisplay(FishyBusiness plugin, Player player) {
        return plugin.getConf().getRewardDisplay(SoundReward.class)
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

}
