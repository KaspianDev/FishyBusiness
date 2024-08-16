package com.github.kaspiandev.fishybusiness;

import com.github.kaspiandev.fishybusiness.area.AreaManager;
import com.github.kaspiandev.fishybusiness.area.FishyArea;
import com.github.kaspiandev.fishybusiness.area.adapter.AreaAdapter;
import com.github.kaspiandev.fishybusiness.config.Config;
import com.github.kaspiandev.fishybusiness.data.Database;
import com.github.kaspiandev.fishybusiness.data.InventoryTable;
import com.github.kaspiandev.fishybusiness.exception.PluginLoadFailureException;
import com.github.kaspiandev.fishybusiness.hook.HookManager;
import com.github.kaspiandev.fishybusiness.listener.AreaEventListener;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutionException;

public final class FishyBusiness extends JavaPlugin {

    private Config config;
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

        Bukkit.getScheduler().runTaskLater(this, () -> {
            Bukkit.getOnlinePlayers().forEach((player) -> {
                try {
                    Inventory inventory = inventoryTable.loadInventory(player).get().get();
                    player.getInventory().setStorageContents(inventory.getStorageContents());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("saved");
        }, 600);

        getServer().getPluginManager().registerEvents(new AreaEventListener(this), this);
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

}
