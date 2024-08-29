package com.github.kaspiandev.fishybusiness.command.subcommand;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.command.SubCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommands;
import com.github.kaspiandev.fishybusiness.config.Message;
import com.github.kaspiandev.fishybusiness.reward.*;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class RewardSubcommand extends SubCommand {

    private final Supplier<List<String>> rewardTypeNameCache;
    private final List<String> weightCache;
    private final List<String> amountCache;
    private final List<String> durationCache;
    private final List<String> messageTypeCache;
    private final List<String> soundCache;
    private final Supplier<List<String>> rewardNameCache;

    public RewardSubcommand(FishyBusiness plugin) {
        super(plugin, SubCommands.REWARD);

        rewardTypeNameCache = Suppliers.memoizeWithExpiration(() -> {
            return RewardTypeRegistry.getRegisteredTypeNames().stream()
                                     .sorted()
                                     .toList();
        }, 30, TimeUnit.SECONDS);

        weightCache = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            weightCache.add(String.valueOf(i));
        }

        amountCache = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            amountCache.add(String.valueOf(i * 5));
        }

        durationCache = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            durationCache.add(String.valueOf(i * 20));
        }

        messageTypeCache = Arrays.stream(MessageReward.Type.values())
                                 .map(MessageReward.Type::name)
                                 .toList();

        soundCache = Arrays.stream(Sound.values())
                           .map(Sound::name)
                           .toList();

        rewardNameCache = Suppliers.memoizeWithExpiration(() -> {
            return plugin.getRewardManager().getRewards().stream()
                         .map(Reward::getName)
                         .toList();
        }, 30, TimeUnit.SECONDS);
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
                                  } else if (rewardClass == ActionBarReward.class) {
                                      handleAddActionbar(sender, name, weight, args);
                                  } else if (rewardClass == TitleReward.class) {
                                      handleAddTitle(sender, name, weight, args);
                                  } else if (rewardClass == ContainerReward.class) {
                                      handleAddContainer(sender, name, weight, args);
                                  } else if (rewardClass == PointsReward.class) {
                                      handleAddPoints(sender, name, weight, args);
                                  } else if (rewardClass == ItemReward.class) {
                                      handleAddItem(sender, name, weight, args);
                                  } else if (rewardClass == POBoxReward.class) {
                                      handleAddPOBox(sender, name, weight, args);
                                  } else if (rewardClass == SoundReward.class) {
                                      handleAddSound(sender, name, weight, args);
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

    private void handleAddPOBox(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NOT_FOUND));
            return;
        }

        String rewardName = args[5];
        if (plugin.getRewardManager().findReward(rewardName).isEmpty()) {
            sender.spigot().sendMessage(plugin.getMessages().get(
                    Message.REWARD_NOT_FOUND,
                    (message) -> message.replace("${rewardName}", rewardName)));
            return;
        }

        plugin.getRewardManager().addReward(new POBoxReward(name, rewardName, weight));
        sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    private void handleAddContainer(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_EMPTY_CONTAINER));
            return;
        }

        List<String> rewardNames = new ArrayList<>();
        for (int i = 5; i < args.length; i++) {
            rewardNames.add(args[i]);
        }

        for (String rewardName : rewardNames) {
            if (plugin.getRewardManager().findReward(rewardName).isEmpty()) {
                sender.spigot().sendMessage(plugin.getMessages().get(
                        Message.REWARD_INVALID_REWARD,
                        (message) -> message.replace("${rewardName}", rewardName)));
                return;
            }
        }

        plugin.getRewardManager().addReward(new ContainerReward(name, rewardNames, weight));
        sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
    }

    private void handleAddItem(CommandSender sender, String name, double weight, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_ONLY_PLAYERS));
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_ITEM, player));
            return;
        }

        plugin.getRewardManager().addReward(new ItemReward(name, item, weight));
        sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
    }

    private void handleAddSound(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_SOUND));
            return;
        }

        try {
            Sound sound = Sound.valueOf(args[5]);

            plugin.getRewardManager().addReward(new SoundReward(name, sound, weight));
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
        } catch (IllegalArgumentException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_SOUND));
        }
    }

    private void handleAddVault(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MONEY));
            return;
        }

        try {
            double amount = Double.parseDouble(args[5]);

            plugin.getRewardManager().addReward(new VaultReward(name, amount, weight));
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
        } catch (NumberFormatException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MONEY));
        }
    }

    private void handleAddPoints(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_POINTS));
            return;
        }

        try {
            int amount = Integer.parseInt(args[5]);

            plugin.getRewardManager().addReward(new PointsReward(name, amount, weight));
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
        } catch (NumberFormatException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_POINTS));
        }
    }

    private void handleAddActionbar(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MESSAGE_TYPE));
            return;
        }

        try {
            ActionBarReward.Type messageType = ActionBarReward.Type.valueOf(args[5]);

            if (args.length == 6) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MESSAGE));
                return;
            }

            StringJoiner message = new StringJoiner(" ");
            for (int i = 6; i < args.length; i++) {
                message.add(args[i]);
            }

            plugin.getRewardManager().addReward(new ActionBarReward(name, message.toString(), messageType, weight));
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
        } catch (IllegalArgumentException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MESSAGE_TYPE));
        }
    }

    private void handleAddTitle(CommandSender sender, String name, double weight, String[] args) {
        if (args.length == 5) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MESSAGE_TYPE));
            return;
        }

        try {
            TitleReward.Type messageType = TitleReward.Type.valueOf(args[5]);

            if (args.length <= 8) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_TITLE_PROPERTIES));
                return;
            }

            int fadeIn = Integer.parseInt(args[6]);
            int stay = Integer.parseInt(args[7]);
            int fadeOut = Integer.parseInt(args[8]);

            if (args.length == 9) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MESSAGE));
                return;
            }

            StringJoiner message = new StringJoiner(" ");
            for (int i = 9; i < args.length; i++) {
                message.add(args[i]);
            }

            plugin.getRewardManager().addReward(new TitleReward(name, message.toString(),
                    null, fadeIn, stay, fadeOut, messageType, weight));
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_ADDED));
        } catch (NumberFormatException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_TITLE_PROPERTIES));
        } catch (IllegalArgumentException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.REWARD_NO_MESSAGE_TYPE));
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

            StringJoiner message = new StringJoiner(" ");
            for (int i = 5; i < args.length; i++) {
                message.add(args[i]);
            }

            plugin.getRewardManager().addReward(new MessageReward(name, message.toString(), messageType, weight));
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

        StringJoiner command = new StringJoiner(" ");
        for (int i = 5; i < args.length; i++) {
            command.add(args[i]);
        }

        plugin.getRewardManager().addReward(new CommandReward(name, command.toString(), weight));
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
        } else if (args[1].equals("add")) {
            if (args.length == 3) {
                return rewardTypeNameCache.get();
            } else if (args.length == 4) {
                return List.of("<name>");
            } else if (args.length == 5) {
                return weightCache;
            } else if (args[2].equals("vault") || args[2].equals("points")) {
                if (args.length == 6) {
                    return amountCache;
                }
            } else if (args[2].equals("message") || args[2].equals("actionbar")) {
                if (args.length == 6) {
                    return messageTypeCache;
                } else if (args.length == 7) {
                    return List.of("<message>");
                }
            } else if (args[2].equals("title")) {
                if (args.length == 6) {
                    return messageTypeCache;
                } else if (args.length < 10) {
                    return durationCache;
                } else if (args.length == 10) {
                    return List.of("<message>");
                }
            } else if (args[2].equals("command")) {
                if (args.length == 6) {
                    return List.of("<command>");
                }
            } else if (args[2].equals("container")) {
                return rewardNameCache.get();
            } else if (args[2].equals("pobox")) {
                if (args.length == 6) {
                    return rewardNameCache.get();
                }
            } else if (args[2].equals("sound")) {
                if (args.length == 6) {
                    return soundCache;
                }
            }
        }
        return List.of();
    }

}
