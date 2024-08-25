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
import com.github.kaspiandev.fishybusiness.listener.AreaFishingListener;
import com.github.kaspiandev.fishybusiness.points.PointManager;
import com.github.kaspiandev.fishybusiness.reward.*;
import com.github.kaspiandev.fishybusiness.reward.adapter.RewardAdapter;
import com.github.kaspiandev.fishybusiness.selector.FishyAreaSelectorFactory;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class FishyBusiness extends JavaPlugin {

    private Config config;
    private Messages messages;
    private Database database;
    private InventoryTable inventoryTable;
    private PointManager pointManager;
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

        RewardTypeRegistry.register("command", new RewardType(CommandReward.class));
        RewardTypeRegistry.register("message", new RewardType(MessageReward.class));
        RewardTypeRegistry.register("actionbar", new RewardType(ActionBarReward.class));
        RewardTypeRegistry.register("title", new RewardType(TitleReward.class));
        RewardTypeRegistry.register("container", new RewardType(ContainerReward.class));

        areaAdapter = new AreaAdapter();
        rewardAdapter = new RewardAdapter();

        try {
            config = new Config(this);
            messages = new Messages(this);
        } catch (PluginLoadFailureException ex) {
            getPluginLoader().disablePlugin(this);
            throw new RuntimeException(ex);
        }

        database = new Database(this);
        inventoryTable = new InventoryTable(database);
        database.registerTable(inventoryTable);

        if (config.isPointsEnabled()) {
            pointManager = new PointManager(this);
        }

        database.load();

        areaManager = new AreaManager(this);
        rewardManager = new RewardManager(this);

        inventoryManager = new InventoryManager(this);

        getServer().getPluginManager().registerEvents(new AreaEventListener(this), this);
        getServer().getPluginManager().registerEvents(new AreaActionListener(this), this);
        getServer().getPluginManager().registerEvents(new AreaFishingListener(this), this);

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
        areaManager.getPlayerAreas().forEach((uuid, area) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;

            inventoryManager.loadSaved(player).join();
        });
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

    public Optional<PointManager> getPointManager() {
        return Optional.ofNullable(pointManager);
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
