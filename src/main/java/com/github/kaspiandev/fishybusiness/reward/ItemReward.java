package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.util.ItemBuilder;
import com.github.kaspiandev.fishybusiness.util.ItemLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemReward implements Reward {

    private final String name;
    private final ItemStack item;
    private final double weight;

    public ItemReward(String name, ItemStack item, double weight) {
        this.name = name;
        this.item = item;
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
        PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), item);
        } else {
            inventory.addItem(item);
        }
    }

    public ItemStack getItem() {
        return item.clone();
    }

    @Override
    public ItemStack getDisplay(FishyBusiness plugin, Player player) {
        return plugin.getConf().getRewardDisplay(ActionBarReward.class)
                     .map((section) -> {
                         ItemStack rewardItem = getItem();
                         ItemBuilder commandBuilder = ItemLoader.loadBuilder(section)
                                                                .type(rewardItem.getType());
                         ItemMeta meta = rewardItem.getItemMeta();
                         if (meta != null) {
                             commandBuilder.name(meta.getDisplayName());
                         }

                         return commandBuilder.build();
                     })
                     .orElse(new ItemStack(Material.AIR));
    }

}
