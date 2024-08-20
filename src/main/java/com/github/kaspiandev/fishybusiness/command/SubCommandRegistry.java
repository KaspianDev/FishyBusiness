package com.github.kaspiandev.fishybusiness.command;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.command.subcommand.AreaSubcommand;
import com.github.kaspiandev.fishybusiness.command.subcommand.ReloadSubcommand;
import com.github.kaspiandev.fishybusiness.command.subcommand.RewardSubcommand;

import java.util.HashMap;
import java.util.Map;

public class SubCommandRegistry {

    private final FishyBusiness plugin;
    private final Map<String, SubCommand> registry;

    public SubCommandRegistry(FishyBusiness plugin) {
        this.plugin = plugin;
        this.registry = new HashMap<>();
        load();
    }

    private void load() {
        ReloadSubcommand reloadSubcommand = new ReloadSubcommand(plugin);
        registry.put(reloadSubcommand.getType().getKey(), reloadSubcommand);
        AreaSubcommand areaSubcommand = new AreaSubcommand(plugin);
        registry.put(areaSubcommand.getType().getKey(), areaSubcommand);
        RewardSubcommand rewardSubcommand = new RewardSubcommand(plugin);
        registry.put(rewardSubcommand.getType().getKey(), rewardSubcommand);
    }

    public Map<String, SubCommand> getRegistry() {
        return registry;
    }

    public SubCommand findById(String id) {
        return registry.get(id);
    }

}
