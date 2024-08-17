package com.github.kaspiandev.fishybusiness;

import com.github.kaspiandev.fishybusiness.area.AreaManager;
import com.github.kaspiandev.fishybusiness.area.FishyArea;
import com.github.kaspiandev.fishybusiness.area.adapter.AreaAdapter;
import com.github.kaspiandev.fishybusiness.command.FishyBusinessCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommandRegistry;
import com.github.kaspiandev.fishybusiness.config.Config;
import com.github.kaspiandev.fishybusiness.config.Messages;
import com.github.kaspiandev.fishybusiness.data.Database;
import com.github.kaspiandev.fishybusiness.data.InventoryTable;
import com.github.kaspiandev.fishybusiness.exception.PluginLoadFailureException;
import com.github.kaspiandev.fishybusiness.hook.HookManager;
import com.github.kaspiandev.fishybusiness.listener.AreaEventListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishyBusiness extends JavaPlugin {

    private Config config;
    private Messages messages;
    private Database database;
    private AreaAdapter areaAdapter;
    private AreaManager areaManager;
    private HookManager hookManager;

    @Override
    public void onEnable() {
        hookManager = new HookManager(this);

        areaAdapter = new AreaAdapter();

        try {
            config = new Config(this);
            messages = new Messages(this);
        } catch (PluginLoadFailureException ex) {
            getPluginLoader().disablePlugin(this);
            throw new RuntimeException(ex);
        }

        areaManager = new AreaManager(this);
        areaManager.addArea(new FishyArea(Bukkit.getWorld("world"), 100, 200, 0, 100, 100, 200));

        database = new Database(this);
        InventoryTable inventoryTable = new InventoryTable(database);
        database.registerTable(inventoryTable);
        database.load();

        getServer().getPluginManager().registerEvents(new AreaEventListener(this), this);

        PluginCommand command = getCommand("fishybusiness");
        if (command != null) {
            SubCommandRegistry subCommandRegistry = new SubCommandRegistry(this);
            FishyBusinessCommand fishyBusinessCommand = new FishyBusinessCommand(this, subCommandRegistry);
            command.setExecutor(fishyBusinessCommand);
            command.setTabCompleter(fishyBusinessCommand);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public HookManager getHookManager() {
        return hookManager;
    }

    public AreaManager getAreaManager() {
        return areaManager;
    }

    public AreaAdapter getAreaAdapter() {
        return areaAdapter;
    }

    public Messages getMessages() {
        return messages;
    }

    public Database getDatabase() {
        return database;
    }

}
