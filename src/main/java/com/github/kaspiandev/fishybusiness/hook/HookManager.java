package com.github.kaspiandev.fishybusiness.hook;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HookManager {

    private final FishyBusiness plugin;
    private final Map<String, Hook<?>> hooks;

    public HookManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.hooks = new HashMap<>();
    }

    public boolean register(String pluginName, Supplier<Hook<?>> hookSupplier) {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled(pluginName)) {
            hooks.put(pluginName, hookSupplier.get());
            return true;
        }
        return false;
    }

}
