package com.github.kaspiandev.fishybusiness.hook;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.area.AreaType;
import com.github.kaspiandev.fishybusiness.area.AreaTypeRegistry;
import com.github.kaspiandev.fishybusiness.area.WorldGuardArea;
import com.github.kaspiandev.fishybusiness.area.adapter.ProtectedRegionAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WorldGuardHook extends Hook<WorldGuardPlugin> {

    public WorldGuardHook(FishyBusiness plugin) {
        super(plugin, "WorldGuard");
    }

    @Override
    protected void load() {
        AreaTypeRegistry.register("worldguard", new AreaType(WorldGuardArea.class, new ProtectedRegionAdapter()));
    }

}
