package com.github.kaspiandev.fishybusiness.command.subcommand;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.command.SubCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommands;
import com.github.kaspiandev.fishybusiness.config.Message;
import com.github.kaspiandev.fishybusiness.points.TopPointEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PointsSubcommand extends SubCommand {

    private final List<String> amountCache;

    public PointsSubcommand(FishyBusiness plugin) {
        super(plugin, SubCommands.POINTS);

        amountCache = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            amountCache.add(String.valueOf(i));
        }
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_NO_ARGUMENTS));
            return;
        }

        String arg = args[1];
        switch (arg) {
            case "give" -> handleGive(sender, args);
            case "remove" -> handleRemove(sender, args);
            case "get" -> handleGet(sender, args);
            case "top" -> handleTop(sender, args);
            default -> sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_INVALID_SUBCOMMAND));
        }
    }

    private void handleTop(CommandSender sender, String[] args) {
        if (args.length > 2) {
            int place = Math.max(1, Math.min(Integer.parseInt(args[2]), 10));

            plugin.getPointManager().ifPresent((pointManager) -> {
                TopPointEntry entry = pointManager.getTopPoints().get(place - 1);
                String name = Bukkit.getOfflinePlayer(entry.uuid()).getName();

                sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_TOP_ENTRY_SINGLE, (format) -> {
                    return format.replace("${name}", (name == null) ? "unknown" : name)
                                 .replace("${points}", String.valueOf(entry.amount()))
                                 .replace("${place}", String.valueOf(place));
                }));
            });
        } else {
            plugin.getPointManager().ifPresent((pointManager) -> {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_TOP_HEADER));
                List<TopPointEntry> topEntries = pointManager.getTopPoints();
                for (int i = 0; i < topEntries.size(); i++) {
                    TopPointEntry entry = topEntries.get(i);
                    String name = Bukkit.getOfflinePlayer(entry.uuid()).getName();

                    int currentPlace = i + 1;
                    sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_TOP_LIST_ENTRY, (format) -> {
                        return format.replace("${name}", (name == null) ? "unknown" : name)
                                     .replace("${points}", String.valueOf(entry.amount()))
                                     .replace("${place}", String.valueOf(currentPlace));
                    }));
                }
            });
        }
    }

    private void handleGet(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_NO_PLAYER));
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_NO_PLAYER));
            return;
        }


        plugin.getPointManager().ifPresent((pointManager) -> {
            int amount = pointManager.getPoints(target);
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_PLAYER_POINTS, (message) -> {
                return message.replace("${amount}", String.valueOf(amount))
                              .replace("${player}", target.getName());
            }));
        });
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_NO_PLAYER));
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_NO_PLAYER));
            return;
        }

        if (args.length == 3) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_REMOVE_NO_AMOUNT));
            return;
        }

        try {
            int amount = Integer.parseInt(args[3]);

            plugin.getPointManager().ifPresent((pointManager) -> {
                pointManager.addPoints(target, -amount);
            });
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_REMOVED, (message) -> {
                return message.replace("${amount}", String.valueOf(amount));
            }));
        } catch (NumberFormatException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_INVALID_POINTS));
        }
    }

    private void handleGive(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_NO_PLAYER));
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_NO_PLAYER));
            return;
        }

        if (args.length == 3) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_GIVE_NO_AMOUNT));
            return;
        }

        try {
            int amount = Integer.parseInt(args[3]);

            plugin.getPointManager().ifPresent((pointManager) -> {
                pointManager.addPoints(target, amount);
            });
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_GIVEN, (message) -> {
                return message.replace("${amount}", String.valueOf(amount));
            }));
        } catch (NumberFormatException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_INVALID_POINTS));
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("add", "remove", "get", "top");
        } else if (args[1].equals("add") || args[1].equals("remove")) {
            if (args.length == 4) {
                return amountCache;
            }
        }
        return List.of();
    }
}
