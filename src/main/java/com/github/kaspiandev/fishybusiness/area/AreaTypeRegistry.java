package com.github.kaspiandev.fishybusiness.area;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AreaTypeRegistry {

    private static final Map<String, AreaType> registry = new HashMap<>();

    static {
        registry.put("FISHY", new AreaType(FishyArea.class));
    }

    private AreaTypeRegistry() {}

    public static void register(String name, AreaType areaType) {
        registry.put(name, areaType);
    }

    public static Optional<AreaType> findByName(String name) {
        return Optional.ofNullable(registry.get(name));
    }

}
