package com.github.kaspiandev.fishybusiness.command.subcommand;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.area.Area;
import com.github.kaspiandev.fishybusiness.area.AreaTypeRegistry;
import com.github.kaspiandev.fishybusiness.area.FishyArea;
import com.github.kaspiandev.fishybusiness.area.exception.AreaOverlapException;
import com.github.kaspiandev.fishybusiness.area.exception.AreaWorldMismatchException;
import com.github.kaspiandev.fishybusiness.command.SubCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommands;
import com.github.kaspiandev.fishybusiness.config.Message;
import com.github.kaspiandev.fishybusiness.pdc.LocationPersistentDataType;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AreaSubcommand extends SubCommand {

    private static final Supplier<List<String>> AREA_TYPE_NAME_CACHE;
    private static final Supplier<List<String>> WORLD_NAME_CACHE;

    static {
        AREA_TYPE_NAME_CACHE = Suppliers.memoizeWithExpiration(() -> {
            return AreaTypeRegistry.getRegisteredTypeNames().stream()
                                   .sorted()
                                   .toList();
        }, 30, TimeUnit.SECONDS);

        WORLD_NAME_CACHE = Suppliers.memoizeWithExpiration(() -> {
            return Bukkit.getWorlds().stream()
                         .map(World::getName)
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
            case "remove" -> handleRemove(sender, args);
            default -> sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_INVALID_SUBCOMMAND));
        }
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length <= 2) {
            if (!(sender instanceof Player player)) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_ONLY_PLAYERS));
                return;
            }

            plugin.getAreaManager().findArea(player.getLocation()).ifPresentOrElse((area) -> {
                plugin.getAreaManager().removeArea(area);
                player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_REMOVED));
            }, () -> {
                player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_NO_AREA));
            });
        } else {
            if (args.length <= 5) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_NO_ARGUMENTS));
                return;
            }

            try {
                World world = Bukkit.getWorld(args[2]);
                double x = Double.parseDouble(args[3]);
                double y = Double.parseDouble(args[4]);
                double z = Double.parseDouble(args[5]);

                plugin.getAreaManager().findArea(new Location(world, x, y, z)).ifPresentOrElse((area) -> {
                    plugin.getAreaManager().removeArea(area);
                    sender.spigot().sendMessage(plugin.getMessages().get(Message.AREA_REMOVED));
                }, () -> {
                    sender.spigot().sendMessage(plugin.getMessages().get(Message.AREA_NO_AREA));
                });
            } catch (NumberFormatException ex) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.AREA_NO_AREA));
            }
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
            case "selector" -> {
                player.getInventory().addItem(plugin.getFishyAreaSelectorFactory().getSelector());
                player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_TOOL_GIVEN));
            }
            case "save" -> {
                ItemStack selector = player.getInventory().getItemInMainHand();
                if (!plugin.getFishyAreaSelectorFactory().isSelector(selector)) {
                    player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_CANNOT_SET));
                    return;
                }

                Location corner1 = plugin.getFishyAreaSelectorFactory().getCorner1(selector);
                if (corner1 == LocationPersistentDataType.INVALID_LOCATION) {
                    player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_CANNOT_SET));
                    return;
                }

                Location corner2 = plugin.getFishyAreaSelectorFactory().getCorner2(selector);
                if (corner2 == LocationPersistentDataType.INVALID_LOCATION) {
                    player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_CANNOT_SET));
                    return;
                }

                try {
                    Area area = new FishyArea(corner1, corner2);
                    plugin.getAreaManager().addArea(area);
                    player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_SAVED));
                } catch (AreaOverlapException ex) {
                    player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_OVERLAP));
                } catch (AreaWorldMismatchException ex) {
                    player.spigot().sendMessage(plugin.getMessages().get(Message.AREA_WORLD_MISMATCH));
                }
            }
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return List.of("add", "remove");
        } else if (args[1].equals("add")) {
            if (args.length == 3) {
                return AREA_TYPE_NAME_CACHE.get();
            } else if (args.length == 4) {
                return List.of("selector", "save");
            }
        } else if (args[1].equals("remove")) {
            if (args.length == 3) {
                return WORLD_NAME_CACHE.get();
            }
            double
            if (args)
        }
        return List.of();
    }

}
