package com.github.kaspiandev.fishybusiness.listener;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.reward.Reward;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class AreaFishingListener implements Listener {

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
                item.setItemStack(reward.getDisplay(plugin, player));
            });
            fishHook.setHookedEntity(hookedItem);
            reward.reward(plugin, player);
        });
    }

}
