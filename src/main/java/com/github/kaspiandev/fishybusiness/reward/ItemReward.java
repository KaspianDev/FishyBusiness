package com.github.kaspiandev.fishybusiness.reward;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
    public void reward(Player player) {
        PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), item);
        } else {
            inventory.addItem(item);
        }
    }

    public ItemStack getItem() {
        return item;
    }

}
