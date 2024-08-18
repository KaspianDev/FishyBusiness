package com.github.kaspiandev.fishybusiness.command.subcommand;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.area.AreaTypeRegistry;
import com.github.kaspiandev.fishybusiness.area.FishyArea;
import com.github.kaspiandev.fishybusiness.command.SubCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommands;
import com.github.kaspiandev.fishybusiness.config.Message;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AreaSubcommand extends SubCommand {

    private static final Supplier<List<String>> AREA_TYPE_NAME_CACHE;

    static {
        AREA_TYPE_NAME_CACHE = Suppliers.memoizeWithExpiration(() -> {
            return AreaTypeRegistry.getRegisteredTypeNames()
                                   .stream()
                                   .sorted()
                                   .toList();
        }, 30, TimeUnit.SECONDS);
    }

    private final FishyBusiness plugin;

    public AreaSubcommand(FishyBusiness plugin) {
        super(plugin, SubCommands.AREA);
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
            // TODO: Remove etc.
            default -> sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_INVALID_SUBCOMMAND));
        }
    }

    private void handleAdd(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_ONLY_PLAYERS));
            return;
        }

        if (args.length <= 2) {
            player.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_NO_ARGUMENTS));
            return;
        }

        AreaTypeRegistry.findByName(args[2])
                        .ifPresentOrElse((areaType) -> {
                            if (areaType.getAreaClass() == FishyArea.class) {
                                handleAddFishy(player, args);
                            } else {
                                player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_UNKNOWN_ADAPTER));
                            }
                        }, () -> {
                            player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_UNKNOWN_ADAPTER));
                        });
    }

    private void handleAddFishy(Player player, String[] args) {
        if (args.length <= 3) {
            player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_NO_NAME));
            return;
        }

        switch (args[3]) {
            case "tool" -> {
                // implement tool
            }
            case "save" -> {
                
            }
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("add");
        } else if (args.length == 3 && args[1].equals("add")) {
            return AREA_TYPE_NAME_CACHE.get();
        }
        return List.of();
    }

}
