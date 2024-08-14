package com.github.kaspiandev.fishybusiness.hook;

import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HookManager {

    private final Map<String, Hook<?>> hooks;

    public HookManager() {
        this.hooks = new HashMap<>();
    }

    public boolean register(String pluginName, Supplier<Hook<?>> hookSupplier) {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled(pluginName)) {
            Hook<?> hook = hookSupplier.get();
            hook.load();
            hooks.put(pluginName, hook);
            return true;
        }
        return false;
    }

    public Collection<Hook<?>> getHooks() {
        return hooks.values();
    }

}
