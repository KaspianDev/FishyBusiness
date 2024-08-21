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
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

public class RewardManager {

    private static final Logger LOGGER = Logger.getLogger(RewardManager.class.getSimpleName());
    private static final Type REWARD_LIST_TYPE = new TypeToken<List<Reward>>() {}.getType();
    private static final Random RANDOM = new Random();

    private final Gson gson;
    private final FishyBusiness plugin;
    private final File rewardFile;
    private final List<Reward> rewards;
    private double totalWeight = 0;

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
        totalWeight += reward.getWeight();
        save();
    }

    public void removeReward(Reward reward) {
        rewards.remove(reward);
        totalWeight -= reward.getWeight();
        save();
    }

    public Optional<Reward> findReward(String name) {
        return rewards.stream()
                      .filter((reward) -> reward.getName().equals(name))
                      .findFirst();
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public Reward chooseRandomReward() {
        double randomNumber = RANDOM.nextDouble() * totalWeight;
        double cumulativeWeight = 0;

        for (Reward reward : rewards) {
            cumulativeWeight += reward.getWeight();
            if (randomNumber < cumulativeWeight) {
                return reward;
            }
        }
        return rewards.get(RANDOM.nextInt(rewards.size())); // Fallback
    }

    private void load() {
        try {
            if (rewardFile.createNewFile()) return;
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            return;
        }

        try (FileReader reader = new FileReader(rewardFile)) {
            List<Reward> loadedRewards = gson.fromJson(reader, REWARD_LIST_TYPE);
            if (loadedRewards != null) {
                rewards.addAll(loadedRewards);
                totalWeight = loadedRewards.stream()
                                           .mapToDouble(Reward::getWeight)
                                           .sum();
            }
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
