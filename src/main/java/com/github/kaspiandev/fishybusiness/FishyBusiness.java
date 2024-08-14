package com.github.kaspiandev.fishybusiness;

import com.github.kaspiandev.fishybusiness.area.AreaManager;
import com.github.kaspiandev.fishybusiness.config.Config;
import com.github.kaspiandev.fishybusiness.exception.PluginLoadFailureException;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishyBusiness extends JavaPlugin {

    private Config config;

    @Override
    public void onEnable() {
        try {
            config = new Config(this);
        } catch (PluginLoadFailureException ex) {
            getPluginLoader().disablePlugin(this);
            throw new RuntimeException(ex);
        }

        new AreaManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
