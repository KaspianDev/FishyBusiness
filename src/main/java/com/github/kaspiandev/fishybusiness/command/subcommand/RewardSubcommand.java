package com.github.kaspiandev.fishybusiness.command.subcommand;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.command.SubCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommands;
import com.github.kaspiandev.fishybusiness.config.Message;
import com.github.kaspiandev.fishybusiness.reward.*;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RewardSubcommand extends SubCommand {

    private static final Supplier<List<String>> REWARD_TYPE_NAME_CACHE;

    static {
        REWARD_TYPE_NAME_CACHE = Suppliers.memoizeWithExpiration(() -> {
            return RewardTypeRegistry.getRegisteredTypeNames().stream()
                                     .sorted()
                                     .toList();
        }, 30, TimeUnit.SECONDS);
    }

    private final FishyBusiness plugin;

    public RewardSubcommand(FishyBusiness plugin) {
        super(plugin, SubCommands.REWARD);
        this.plugin = plugin;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_NO_ARGUMENTS));
            return;
        }

        switch (args[1]) {
            case "add" -> handleAdd(sender, args);
            case "remove" -> handleRemove(sender, args);
            default -> sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_INVALID_SUBCOMMAND));
        }
    }

    private void handleAdd(CommandSender sender, String[] args) {
        if (args.length <= 2) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_UNKNOWN_ADAPTER));
            return;
        }

        if (args.length == 3) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_NAME));
            return;
        }

        String name = args[3];

        if (plugin.getRewardManager().findReward(name).isPresent()) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_OVERLAP));
            return;
        }

        if (args.length == 4) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_WEIGHT));
            return;
        }

        try {
            double weight = Double.parseDouble(args[4]);

            RewardTypeRegistry.findByName(args[2])
                              .ifPresentOrElse((rewardType) -> {
                                  Class<? extends Reward> rewardClass = rewardType.getAreaClass();
                                  if (rewardClass == CommandReward.class) {
                                      handleAddCommand(sender, name, weight, args);
                                  } else if (rewardClass == MessageReward.class) {
                                      handleAddMessage(sender, name, weight, args);
                                  } else if (rewardClass == VaultReward.class) {
                                      handleAddVault(sender, name, weight, args);
                                  } else {
                                      sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_UNKNOWN_ADAPTER));
                                  }
                              }, () -> {
                                  sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_UNKNOWN_ADAPTER));
                              });
        } catch (NumberFormatException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_WEIGHT));
        }
    }

    private void handleAddVault(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_AMOUNT));
            return;
        }

        try {
            double amount = Double.parseDouble(args[5]);

            plugin.getRewardManager().addReward(new VaultReward(plugin, name, amount, weight));
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
        } catch (NumberFormatException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_AMOUNT));
        }
    }

    private void handleAddMessage(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MESSAGE_TYPE));
            return;
        }

        try {
            MessageReward.Type messageType = MessageReward.Type.valueOf(args[5]);

            if (args.length == 6) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MESSAGE));
                return;
            }

            StringBuilder message = new StringBuilder();
            for (int i = 5; i < args.length; i++) {
                message.append(args[i]);
            }

            plugin.getRewardManager().addReward(new MessageReward(plugin, name, message.toString(), messageType, weight));
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
        } catch (IllegalArgumentException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MESSAGE_TYPE));
        }
    }

    private void handleAddCommand(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_COMMAND));
            return;
        }

        StringBuilder command = new StringBuilder();
        for (int i = 4; i < args.length; i++) {
            command.append(args[i]);
        }

        plugin.getRewardManager().addReward(new CommandReward(plugin, name, command.toString(), weight));
        sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length <= 2) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_NAME));
            return;
        }

        if (!(sender instanceof Player player)) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_ONLY_PLAYERS));
            return;
        }

        plugin.getRewardManager().findReward(args[2]).ifPresentOrElse((reward) -> {
            plugin.getRewardManager().removeReward(reward);
            player.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_REMOVED));
        }, () -> {
            player.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NOT_FOUND));
        });
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("add", "remove");
        }
        return List.of();
    }

}
