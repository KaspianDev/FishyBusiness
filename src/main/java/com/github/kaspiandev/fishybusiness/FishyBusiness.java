package com.github.kaspiandev.fishybusiness;

import com.github.kaspiandev.fishybusiness.area.AreaManager;
import com.github.kaspiandev.fishybusiness.area.FishyArea;
import com.github.kaspiandev.fishybusiness.config.Config;
import com.github.kaspiandev.fishybusiness.exception.PluginLoadFailureException;
import com.github.kaspiandev.fishybusiness.listener.AreaEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishyBusiness extends JavaPlugin {

    private Config config;
    private AreaManager areaManager;

    @Override
    public void onEnable() {
        try {
            config = new Config(this);
        } catch (PluginLoadFailureException ex) {
            getPluginLoader().disablePlugin(this);
            throw new RuntimeException(ex);
        }

        areaManager = new AreaManager(this);
        areaManager.addArea(new FishyArea(Bukkit.getWorld("world"), 100, 200, 0, 100, 100, 200));

        // TODO: Check for enabled adapters from config

        getServer().getPluginManager().registerEvents(new AreaEventListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public AreaManager getAreaManager() {
        return areaManager;
    }

}
