package com.github.kaspiandev.fishybusiness.inventory;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.inventory.exception.InventoryInvalidMaskException;
import com.github.kaspiandev.fishybusiness.util.ItemLoader;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InventoryManager {

    private static final Cache<UUID, ItemStack[]> INVENTORY_CACHE;

    static {
        INVENTORY_CACHE = CacheBuilder.newBuilder()
                                      .expireAfterWrite(30, TimeUnit.SECONDS)
                                      .maximumSize(100)
                                      .build();
    }

    private final FishyBusiness plugin;
    private final List<ItemStack> fishyInventory;

    public InventoryManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.fishyInventory = buildInventory();
    }

    private List<ItemStack> buildInventory() {
        YamlDocument document = plugin.getConf().getDocument();
        List<String> storageMask = document.getStringList("inventory.mask.storage");
        if (storageMask.size() != 3) {
            throw new InventoryInvalidMaskException("There must be exactly 3 lines of storage mask in configuration.");
        }

        List<String> hotbarMask = document.getStringList("inventory.mask.hotbar");
        if (hotbarMask.size() != 1) {
            throw new InventoryInvalidMaskException("There must be exactly 1 line of hotbar mask in configuration.");
        }

        List<ItemStack> items = new ArrayList<>();
        items.addAll(parseMask(hotbarMask, document));
        items.addAll(parseMask(storageMask, document));

        return items;
    }


    private List<ItemStack> parseMask(List<String> mask, YamlDocument document) {
        List<ItemStack> items = new ArrayList<>();
        for (String maskLine : mask) {
            if (maskLine.length() != 9) {
                throw new InventoryInvalidMaskException("Each mask line must be exactly 9 characters.");
            }

            for (int i = 0; i < 9; i++) {
                char itemChar = maskLine.charAt(i);
                ItemStack item = ItemLoader.loadItem(document.getSection("inventory.items." + itemChar));
                items.add(item);
            }
        }
        return items;
    }

    public void apply(Player player) {
        PlayerInventory inventory = player.getInventory();
        INVENTORY_CACHE.put(player.getUniqueId(), inventory.getContents());

        plugin.getInventoryTable().saveInventory(player).thenRun(() -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                inventory.clear(); // TODO: Make this configurable (let players use their own armor)
                for (int i = 0; i < 9 * 4; i++) {
                    inventory.setItem(i, fishyInventory.get(i));
                }
            });
        });
    }

    // TODO: modify database to only load 4 storage rows
    public void loadSaved(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] cachedInventory = INVENTORY_CACHE.getIfPresent(player.getUniqueId());
        if (cachedInventory != null) {
            inventory.setContents(cachedInventory);
            return;
        }

        plugin.getInventoryTable().loadInventory(player).thenAccept((optItems) -> {
            optItems.ifPresent((items) -> {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    inventory.setContents(items);
                });
            });
        });
    }

}
