package com.github.kaspiandev.fishybusiness.reward;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.reward.adapter.RewardListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RewardManager {

    private static final Logger LOGGER = Logger.getLogger(RewardManager.class.getSimpleName());
    private static final Type REWARD_LIST_TYPE = new TypeToken<List<Reward>>() {}.getType();

    private final Gson gson;
    private final FishyBusiness plugin;
    private final File rewardFile;
    private final List<Reward> rewards;

    public RewardManager(FishyBusiness plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Reward.class, plugin.getRewardAdapter())
                .registerTypeAdapter(REWARD_LIST_TYPE, new RewardListAdapter())
                .create();
        this.rewardFile = new File(plugin.getDataFolder(), "rewards.json");
        this.rewards = new ArrayList<>();
        load();
    }

    public void addReward(Reward reward) {
        rewards.add(reward);
        save();
    }

    public void removeReward(Reward reward) {
        rewards.remove(reward);
        save();
    }

    private void load() {
        try {
            if (rewardFile.createNewFile()) return;
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            return;
        }

        try (FileReader reader = new FileReader(rewardFile)) {
            List<Reward> loadedAreas = gson.fromJson(reader, REWARD_LIST_TYPE);
            if (loadedAreas != null) rewards.addAll(loadedAreas);
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(rewardFile)) {
            writer.write(gson.toJson(rewards, REWARD_LIST_TYPE));
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

}
