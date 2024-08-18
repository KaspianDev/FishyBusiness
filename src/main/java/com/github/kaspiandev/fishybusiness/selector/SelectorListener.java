package com.github.kaspiandev.fishybusiness.selector;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SelectorListener implements Listener {

    private final FishyBusiness plugin;

    public SelectorListener(FishyBusiness plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        EquipmentSlot hand = event.getHand();
        if (hand == null || hand == EquipmentSlot.OFF_HAND) return;

        Player player = event.getPlayer();
        ItemStack selector = player.getInventory().getItemInMainHand();
        if (!plugin.getFishyAreaSelectorFactory().isSelector(selector)) return;

        Action action = event.getAction();
        Block block = event.getClickedBlock();
        if (action == Action.LEFT_CLICK_BLOCK) {
            assert block != null;

            plugin.getFishyAreaSelectorFactory().setCorner1(selector, event.getClickedBlock().getLocation());
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            assert block != null;

            plugin.getFishyAreaSelectorFactory().setCorner2(selector, event.getClickedBlock().getLocation());
        }
    }

}
