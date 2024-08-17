package com.github.kaspiandev.fishybusiness.hook.worldguard;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.Hook;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends Hook<PlaceholderAPIPlugin> {

    public PlaceholderAPIHook(FishyBusiness plugin) {
        super(plugin, "PlaceholderAPI");
    }

    @Override
    protected void load() {}

    public String applyPlaceholders(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

}
