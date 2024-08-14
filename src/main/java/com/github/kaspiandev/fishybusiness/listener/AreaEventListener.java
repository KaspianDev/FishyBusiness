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
        System.out.println(previousArea.isPresent());
        Optional<Area> newArea = plugin.getAreaManager().findArea(to);
        System.out.println(newArea.isPresent());
        if (newArea.isEmpty()) {
            if (previousArea.isPresent()) {
                Bukkit.getServer().getPluginManager().callEvent(new AreaLeaveEvent(event.getPlayer(), previousArea.get()));
            } else {
                return;
            }
        }

        if (previousArea.isPresent()) {
            System.out.println("pre");
            return;
        }

        System.out.println("good");
        Bukkit.getServer().getPluginManager().callEvent(new AreaEnterEvent(event.getPlayer(), newArea.get()));
    }

    @EventHandler
    public void onAreaEnter(AreaEnterEvent event) {
        System.out.println("enter " + event.getArea().getMinCorner() + " " + event.getArea().getMaxCorner());
    }

    @EventHandler
    public void onAreaLeave(AreaLeaveEvent event) {
        System.out.println("leave " + event.getArea().getMinCorner() + " " + event.getArea().getMaxCorner());
    }


}
