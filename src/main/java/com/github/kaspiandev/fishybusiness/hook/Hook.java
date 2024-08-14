package com.github.kaspiandev.fishybusiness.hook;

import org.bukkit.plugin.Plugin;

public abstract class Hook<T extends Plugin> {

    private final T plugin;

    public Hook(T plugin) {
        this.plugin = plugin;
    }

    protected abstract void load();

}
