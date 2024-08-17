package com.github.kaspiandev.fishybusiness.hook;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import org.bukkit.Bukkit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class HookManager {

    private final FishyBusiness plugin;
    private final Map<Class<?>, Hook<?>> hooks;

    public HookManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.hooks = new HashMap<>();
    }

    public boolean register(String pluginName, Function<FishyBusiness, Hook<?>> hookFunction) {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled(pluginName)) {
            Hook<?> hook = hookFunction.apply(plugin);
            hook.load();
            hooks.put(hook.getClass(), hook);
            return true;
        }
        return false;
    }

    public Collection<Hook<?>> getHooks() {
        return hooks.values();
    }

    public <T extends Hook<?>> Optional<T> findHook(Class<? extends T> hookClass) {
        return Optional.ofNullable(hookClass.cast(hooks.get(hookClass)));
    }

}
