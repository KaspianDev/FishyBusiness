package com.github.kaspiandev.fishybusiness.listener;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.area.Area;
import com.github.kaspiandev.fishybusiness.event.AreaEnterEvent;
import com.github.kaspiandev.fishybusiness.event.AreaLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class AreaEventListener implements Listener {

    private final FishyBusiness plugin;

    public AreaEventListener(FishyBusiness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnter(PlayerMoveEvent event) {
        Location to = event.getTo();
        if (to == null) return;

        Location from = event.getFrom();
        Optional<Area> previousArea = plugin.getAreaManager().findArea(from);
        Optional<Area> newArea = plugin.getAreaManager().findArea(to);
        if (newArea.isEmpty()) {
            if (previousArea.isPresent()) {
                Bukkit.getServer().getPluginManager().callEvent(new AreaLeaveEvent(event.getPlayer(), previousArea.get()));
            }
            return;
        }

        if (previousArea.isPresent() && previousArea.get() == newArea.get()) {
            return;
        }

        Bukkit.getServer().getPluginManager().callEvent(new AreaEnterEvent(event.getPlayer(), newArea.get()));
    }

}
