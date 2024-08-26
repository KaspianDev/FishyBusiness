package com.github.kaspiandev.fishybusiness.command.subcommand;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.command.SubCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommands;
import com.github.kaspiandev.fishybusiness.config.Message;
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
            default -> sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_INVALID_SUBCOMMAND));
        }
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
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_REMOVED));
        } catch (NumberFormatException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_REMOVE_NO_AMOUNT));
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
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_GIVEN));
        } catch (NumberFormatException ex) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.POINTS_GIVE_NO_AMOUNT));
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("add", "remove");
        } else if (args[1].equals("add") || args[1].equals("remove")) {
            if (args.length == 4) {
                return amountCache;
            }
        }
        return List.of();
    }
}
