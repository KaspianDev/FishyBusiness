package com.github.kaspiandev.fishybusiness.hook;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import org.bukkit.plugin.Plugin;

public abstract class Hook<T extends Plugin> {

    protected final FishyBusiness plugin;
    protected final T hookPlugin;

    public Hook(FishyBusiness plugin, T hookPlugin) {
        this.plugin = plugin;
        this.hookPlugin = hookPlugin;
    }

    protected abstract void load();

}
