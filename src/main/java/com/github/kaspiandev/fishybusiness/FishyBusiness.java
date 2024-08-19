package com.github.kaspiandev.fishybusiness;

import com.github.kaspiandev.fishybusiness.area.AreaManager;
import com.github.kaspiandev.fishybusiness.area.adapter.AreaAdapter;
import com.github.kaspiandev.fishybusiness.command.FishyBusinessCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommandRegistry;
import com.github.kaspiandev.fishybusiness.config.Config;
import com.github.kaspiandev.fishybusiness.config.Messages;
import com.github.kaspiandev.fishybusiness.data.Database;
import com.github.kaspiandev.fishybusiness.data.InventoryTable;
import com.github.kaspiandev.fishybusiness.exception.PluginLoadFailureException;
import com.github.kaspiandev.fishybusiness.hook.HookManager;
import com.github.kaspiandev.fishybusiness.inventory.InventoryManager;
import com.github.kaspiandev.fishybusiness.listener.AreaActionListener;
import com.github.kaspiandev.fishybusiness.listener.AreaEventListener;
import com.github.kaspiandev.fishybusiness.reward.*;
import com.github.kaspiandev.fishybusiness.reward.adapter.FishyBusinessAdapter;
import com.github.kaspiandev.fishybusiness.reward.adapter.RewardAdapter;
import com.github.kaspiandev.fishybusiness.selector.FishyAreaSelectorFactory;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishyBusiness extends JavaPlugin {

    private Config config;
    private Messages messages;
    private Database database;
    private InventoryTable inventoryTable;
    private AreaAdapter areaAdapter;
    private RewardAdapter rewardAdapter;
    private RewardManager rewardManager;
    private AreaManager areaManager;
    private HookManager hookManager;
    private FishyAreaSelectorFactory fishyAreaSelectorFactory;
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        hookManager = new HookManager(this);

        FishyBusinessAdapter fishyBusinessAdapter = new FishyBusinessAdapter(this);
        RewardTypeRegistry.register("command", new RewardType(CommandReward.class, fishyBusinessAdapter));

        areaAdapter = new AreaAdapter();
        rewardAdapter = new RewardAdapter();

        try {
            config = new Config(this);
            messages = new Messages(this);
        } catch (PluginLoadFailureException ex) {
            getPluginLoader().disablePlugin(this);
            throw new RuntimeException(ex);
        }

        areaManager = new AreaManager(this);
        rewardManager = new RewardManager(this);
        rewardManager.addReward(new CommandReward(this, "say hi, %player_name%", 50));
        rewardManager.addReward(new MessageReward(this, "%player_name% won something!", MessageReward.Type.BROADCAST, 30));
        rewardManager.addReward(new MessageReward(this, "%player_name%, wow you won!", MessageReward.Type.BROADCAST, 20));

        database = new Database(this);
        inventoryTable = new InventoryTable(database);
        database.registerTable(inventoryTable);
        database.load();

        inventoryManager = new InventoryManager(this);

        getServer().getPluginManager().registerEvents(new AreaEventListener(this), this);
        getServer().getPluginManager().registerEvents(new AreaActionListener(this), this);

        fishyAreaSelectorFactory = new FishyAreaSelectorFactory(this);

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

    public RewardManager getRewardManager() {
        return rewardManager;
    }

    public Messages getMessages() {
        return messages;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public Config getConf() {
        return config;
    }

    public FishyAreaSelectorFactory getFishyAreaSelectorFactory() {
        return fishyAreaSelectorFactory;
    }

    public RewardAdapter getRewardAdapter() {
        return rewardAdapter;
    }

    public Database getDatabase() {
        return database;
    }

    public InventoryTable getInventoryTable() {
        return inventoryTable;
    }

}
