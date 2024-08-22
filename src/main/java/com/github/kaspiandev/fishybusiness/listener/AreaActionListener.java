package com.github.kaspiandev.fishybusiness.listener;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.event.AreaEnterEvent;
import com.github.kaspiandev.fishybusiness.event.AreaLeaveEvent;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.PlayerInventory;

public class AreaActionListener implements Listener {

    private final FishyBusiness plugin;

    public AreaActionListener(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnter(AreaEnterEvent event) {
        Player player = event.getPlayer();

        plugin.getInventoryManager().apply(player);
    }

    @EventHandler
    public void onLeave(AreaLeaveEvent event) {
        Player player = event.getPlayer();

        plugin.getInventoryManager().loadSaved(player);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getClickedInventory() instanceof PlayerInventory)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (plugin.getAreaManager().getPlayerArea(player).isPresent()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getInventory() instanceof PlayerInventory)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (plugin.getAreaManager().getPlayerArea(player).isPresent()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Item item = event.getItem();
        if (item.hasMetadata(AreaFishingListener.REWARD_META)) {
            item.remove();
            event.setCancelled(true);
            return;
        }

        if (plugin.getAreaManager().getPlayerArea(player).isPresent()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (plugin.getAreaManager().getPlayerArea(player).isPresent()) {
            event.setCancelled(true);
        }
    }

}
