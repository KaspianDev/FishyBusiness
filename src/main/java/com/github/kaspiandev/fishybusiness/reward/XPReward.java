package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.util.ComponentUtil;
import com.github.kaspiandev.fishybusiness.util.ItemBuilder;
import com.github.kaspiandev.fishybusiness.util.ItemLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class XPReward implements Reward {

    private final String name;
    private final int amount;
    private final double weight;

    public XPReward(String name, int amount, double weight) {
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
    public void reward(FishyBusiness plugin, Player player) {
        player.giveExp(amount);
    }

    @Override
    public ItemStack getDisplay(FishyBusiness plugin, Player player) {
        return plugin.getConf().getRewardDisplay(XPReward.class)
                     .map((section) -> {
                         ItemBuilder commandBuilder = ItemLoader.loadBuilder(section);
                         String name = commandBuilder.getName();
                         if (name != null) {
                             commandBuilder.name(ComponentUtil.toString(plugin.getMessages().get(name, (oldName) -> {
                                 return oldName
                                         .replace("${name}", this.name)
                                         .replace("${amount}", String.valueOf(amount));
                             }, player)));
                         }
                         return commandBuilder.build();
                     })
                     .orElse(new ItemStack(Material.AIR));
    }

}
