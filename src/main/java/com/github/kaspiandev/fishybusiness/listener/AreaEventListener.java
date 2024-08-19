package com.github.kaspiandev.fishybusiness.listener;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.area.Area;
import com.github.kaspiandev.fishybusiness.event.AreaEnterEvent;
import com.github.kaspiandev.fishybusiness.event.AreaLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        Player player = event.getPlayer();
        if (newArea.isEmpty()) {
            if (previousArea.isPresent()) {
                plugin.getAreaManager().clearPlayerArea(player);
                Bukkit.getServer().getPluginManager().callEvent(new AreaLeaveEvent(player, previousArea.get()));
            }
            return;
        }

        if (previousArea.isPresent() && previousArea.get() == newArea.get()) {
            return;
        }

        plugin.getAreaManager().putPlayerArea(player, newArea.get());
        Bukkit.getServer().getPluginManager().callEvent(new AreaEnterEvent(player, newArea.get()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getAreaManager().findArea(player.getLocation()).ifPresent((area) -> {
            plugin.getAreaManager().putPlayerArea(player, area);
            Bukkit.getServer().getPluginManager().callEvent(new AreaEnterEvent(player, area));
        });
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getAreaManager().getPlayerArea(player).ifPresent((area) -> {
            plugin.getAreaManager().clearPlayerArea(event.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(new AreaLeaveEvent(player, area));
        });
    }

}
