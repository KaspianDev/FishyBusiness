package com.github.kaspiandev.fishybusiness.util;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemLoader {

    private ItemLoader() {}

    public static ItemBuilder loadBuilder(Section section) {
        try {
            Material type = Material.valueOf(section.getString("type", "AIR"));
            ItemBuilder itemBuilder = new ItemBuilder(type);

            section.getOptionalBoolean("unbreakable").ifPresent(itemBuilder::unbreakable);

            section.getOptionalString("name").ifPresent(itemBuilder::name);

            section.getOptionalStringList("lore").ifPresent(itemBuilder::lore);

            section.getOptionalStringList("flags").ifPresent((flags) -> {
                for (String flag : flags) {
                    try {
                        ItemFlag itemFlag = ItemFlag.valueOf(flag);
                        itemBuilder.flag(itemFlag);
                    } catch (IllegalArgumentException ignored) {}
                }
            });

            section.getOptionalMapList("enchants").ifPresent((enchants) -> {
                for (Map<?, ?> enchant : enchants) {
                    Enchantment enchantmentType = Enchantment.getByKey(NamespacedKey.minecraft((String) enchant.get("type")));
                    if (enchantmentType == null) continue;

                    int level = (int) enchant.get("level");
                    itemBuilder.enchant(enchantmentType, level);
                }
            });

            return itemBuilder;
        } catch (IllegalArgumentException ex) {
            return new ItemBuilder(Material.AIR);
        }
    }

    public static ItemStack loadItem(Section section) {
        return loadBuilder(section).build();
    }

}
