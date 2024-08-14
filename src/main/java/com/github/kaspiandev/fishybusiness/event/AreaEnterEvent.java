package com.github.kaspiandev.fishybusiness.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class AreaEnterEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public AreaEnterEvent(@NotNull Player who) {
        super(who);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
