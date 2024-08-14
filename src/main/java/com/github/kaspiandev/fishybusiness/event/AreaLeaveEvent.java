package com.github.kaspiandev.fishybusiness.event;

import com.github.kaspiandev.fishybusiness.area.Area;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class AreaLeaveEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Area area;

    public AreaLeaveEvent(@NotNull Player who, Area area) {
        super(who);
        this.area = area;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public Area getArea() {
        return area;
    }

}
