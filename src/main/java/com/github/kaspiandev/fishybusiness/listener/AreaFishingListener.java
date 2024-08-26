package com.github.kaspiandev.fishybusiness.listener;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.reward.Reward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class AreaFishingListener implements Listener {

    public static final String REWARD_META = "fishybusiness-reward";

    private final FishyBusiness plugin;

    public AreaFishingListener(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCatch(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        Player player = event.getPlayer();
        plugin.getAreaManager().getPlayerArea(player).ifPresent((area) -> {
            FishHook fishHook = event.getHook();
            if (!area.isInside(fishHook.getLocation())) return;

            Entity caught = event.getCaught();
            if (caught instanceof Item) caught.remove();

            Reward reward = plugin.getRewardManager().chooseRandomReward();

            Item hookedItem = player.getWorld().spawn(fishHook.getLocation(), Item.class, (item) -> {
                ItemStack display = reward.getDisplay(plugin, player);
                item.setItemStack(display);

                ItemMeta displayMeta = display.getItemMeta();
                if (displayMeta != null) {
                    item.setOwner(player.getUniqueId());
                    if (displayMeta.hasDisplayName()) {
                        item.setCustomName(displayMeta.getDisplayName());
                        item.setCustomNameVisible(true);
                    }
                }

                item.setMetadata(REWARD_META, new FixedMetadataValue(plugin, true));
            });

            Bukkit.getScheduler().runTaskLater(plugin, hookedItem::remove, 20);

            fishHook.setHookedEntity(hookedItem);
            hookedItem.setVelocity(player.getLocation().add(0, 1.5, 0).getDirection().normalize().multiply(-0.85));
            reward.reward(plugin, player);
        });
    }

}
