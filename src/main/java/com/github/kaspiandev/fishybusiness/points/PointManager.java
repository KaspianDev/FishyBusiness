package com.github.kaspiandev.fishybusiness.points;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.data.PointsTable;
import com.github.kaspiandev.fishybusiness.reward.ContainerReward;
import com.github.kaspiandev.fishybusiness.reward.RewardType;
import com.github.kaspiandev.fishybusiness.reward.RewardTypeRegistry;
import com.github.kaspiandev.fishybusiness.reward.adapter.PointsRewardAdapter;

public class PointManager {

    private final FishyBusiness plugin;
    private final PointsTable pointsTable;

    public PointManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.pointsTable = new PointsTable(plugin.getDatabase());
        plugin.getDatabase().registerTable(pointsTable);

        PointsRewardAdapter pointsRewardAdapter = new PointsRewardAdapter(plugin);
        RewardType pointsRewardType = new RewardType(ContainerReward.class, pointsRewardAdapter);
        RewardTypeRegistry.register("points", pointsRewardType);
    }

    public PointsTable getPointsTable() {
        return pointsTable;
    }

}
