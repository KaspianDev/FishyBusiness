package com.github.kaspiandev.fishybusiness.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private final Material type;
    private int amount = 1;
    private boolean unbreakable = false;
    private String name;
    private List<String> lore;
    private Map<Enchantment, Integer> enchants;
    private List<ItemFlag> itemFlags;

    public ItemBuilder(Material type) {
        this.type = type;
    }

    public static ItemBuilder ofType(Material type) {
        return new ItemBuilder(type);
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = ColorUtil.string(name);
        return this;
    }

    public ItemBuilder unbreakable() {
        return unbreakable(true);
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = ColorUtil.string(lore);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        if (this.enchants == null) this.enchants = new HashMap<>();

        this.enchants.put(enchantment, level);
        return this;
    }

    public ItemBuilder flags(List<ItemFlag> itemFlags) {
        itemFlags.forEach(this::flag);
        return this;
    }

    public ItemBuilder flag(ItemFlag itemFlag) {
        if (this.itemFlags == null) this.itemFlags = new ArrayList<>();

        this.itemFlags.add(itemFlag);
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(type);
        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.setUnbreakable(unbreakable);

        if (name != null) {
            meta.setDisplayName(name);
        }

        if (lore != null) {
            meta.setLore(lore);
        }

        if (enchants != null) {
            enchants.forEach(((enchantment, level) -> {
                meta.addEnchant(enchantment, level, true);
            }));
        }

        if (itemFlags != null) {
            meta.addItemFlags(itemFlags.toArray(ItemFlag[]::new));
        }

        item.setItemMeta(meta);

        return item;
    }

}
