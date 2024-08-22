package com.github.kaspiandev.fishybusiness.points;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.data.PointsTable;
import com.github.kaspiandev.fishybusiness.reward.PointsReward;
import com.github.kaspiandev.fishybusiness.reward.RewardType;
import com.github.kaspiandev.fishybusiness.reward.RewardTypeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PointManager implements Listener {

    private final FishyBusiness plugin;
    private final PointsTable pointsTable;
    private final Map<UUID, Integer> playerPoints;
    private final List<TopPointEntry> topPoints; // Redo to use arraylist

    public PointManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.pointsTable = new PointsTable(plugin.getDatabase());
        plugin.getDatabase().registerTable(pointsTable);
        this.playerPoints = new HashMap<>();
        this.topPoints = new ArrayList<>();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::refreshTopPoints, 0, 1200);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        RewardType pointsRewardType = new RewardType(PointsReward.class);
        RewardTypeRegistry.register("points", pointsRewardType);
    }

    private void refreshTopPoints() {
        topPoints.clear();
        pointsTable.getTopPoints().thenAccept(this.topPoints::addAll);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        pointsTable.getPoints(player).thenAccept((points) -> playerPoints.put(uuid, points));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playerPoints.remove(event.getPlayer().getUniqueId());
    }

    public PointsTable getPointsTable() {
        return pointsTable;
    }

    public List<TopPointEntry> getTopPoints() {
        return topPoints;
    }

    public int getPoints(Player player) {
        return playerPoints.getOrDefault(player.getUniqueId(), 0);
    }

    public void addPoints(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        if (playerPoints.containsKey(uuid)) {
            playerPoints.put(uuid, playerPoints.get(uuid) + amount);
            pointsTable.addPoints(player, amount);
            return;
        }
        playerPoints.put(uuid, amount);
        pointsTable.addPoints(player, amount);
    }

}
