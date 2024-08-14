package com.github.kaspiandev.fishybusiness.hook;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class Hook<T extends Plugin> {

    protected final FishyBusiness plugin;
    protected final T hookPlugin;

    @SuppressWarnings("unchecked")
    public Hook(FishyBusiness plugin, String hookPluginName) {
        this.plugin = plugin;
        this.hookPlugin = (T) Bukkit.getPluginManager().getPlugin(hookPluginName);
    }

    protected abstract void load();

}
