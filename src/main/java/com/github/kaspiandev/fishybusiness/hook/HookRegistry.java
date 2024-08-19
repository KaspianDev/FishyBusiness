package com.github.kaspiandev.fishybusiness.hook;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.worldguard.PlaceholderAPIHook;
import com.github.kaspiandev.fishybusiness.hook.worldguard.VaultHook;
import com.github.kaspiandev.fishybusiness.hook.worldguard.WorldGuardHook;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class HookRegistry {

    private static final Map<String, Function<FishyBusiness, Hook<?>>> registry = new HashMap<>();

    static {
        registry.put("WorldGuard", WorldGuardHook::new);
        registry.put("PlaceholderAPI", PlaceholderAPIHook::new);
        registry.put("Vault", VaultHook::new);
    }

    private HookRegistry() {}

    public static void register(String name, Function<FishyBusiness, Hook<?>> hookFunction) {
        registry.put(name, hookFunction);
    }

    public static Optional<Function<FishyBusiness, Hook<?>>> findByName(String name) {
        return Optional.ofNullable(registry.get(name));
    }

}
