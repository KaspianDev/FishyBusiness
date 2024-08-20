package com.github.kaspiandev.fishybusiness.reward;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RewardTypeRegistry {

    private static final Map<String, RewardType> registry = new HashMap<>();

    private RewardTypeRegistry() {}

    public static void register(String name, RewardType areaType) {
        registry.put(name, areaType);
    }

    public static Optional<RewardType> findByName(String name) {
        return Optional.ofNullable(registry.get(name));
    }

    public static Set<String> getRegisteredTypeNames() {
        return registry.keySet();
    }

}
