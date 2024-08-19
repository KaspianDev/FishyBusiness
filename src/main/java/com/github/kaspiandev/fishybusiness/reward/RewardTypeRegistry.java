package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.reward.adapter.FishyBusinessAdapter;
import com.github.kaspiandev.fishybusiness.reward.adapter.MessageTypeAdapter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RewardTypeRegistry {

    private static final Map<String, RewardType> registry = new HashMap<>();

    static {
        registry.put("message", new RewardType(MessageReward.class, new MessageTypeAdapter(), new FishyBusinessAdapter((FishyBusiness) Bukkit.getPluginManager().getPlugin("FishyBusiness"))));
    }

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
