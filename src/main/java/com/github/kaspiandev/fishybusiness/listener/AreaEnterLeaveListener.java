package com.github.kaspiandev.fishybusiness.listener;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.event.AreaEnterEvent;
import com.github.kaspiandev.fishybusiness.event.AreaLeaveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AreaEnterLeaveListener implements Listener {

    private final FishyBusiness plugin;

    public AreaEnterLeaveListener(FishyBusiness plugin) {
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


}
