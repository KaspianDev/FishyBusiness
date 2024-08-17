package com.github.kaspiandev.fishybusiness.command.subcommand;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.command.SubCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommands;
import com.github.kaspiandev.fishybusiness.config.Message;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ReloadSubcommand extends SubCommand {

    private static final List<String> RELOADABLE_COMPONENTS = List.of(
            "config",
            "messages"
    );

    private final FishyBusiness plugin;

    public ReloadSubcommand(FishyBusiness plugin) {
        super(plugin, SubCommands.RELOAD);
        this.plugin = plugin;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            String component = args[1];

            YamlDocument document = switch (component) {
                case "config" -> plugin.getConf().getDocument();
                case "messages" -> plugin.getMessages().getDocument();
                default -> null;
            };

            if (document == null) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.RELOAD_UNKNOWN_TYPE));
                return;
            }

            try {
                document.reload();
                sender.spigot().sendMessage(plugin.getMessages().get(Message.RELOAD_RELOADED, (message) -> {
                    return message.replace("${file}", Objects.requireNonNull(document.getFile()).getName());
                }));
            } catch (IOException e) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.RELOAD_ERROR, (message) -> {
                    return message.replace("${file}", Objects.requireNonNull(document.getFile()).getName());
                }));
            }
        } else {
            try {
                YamlDocument configDocument = plugin.getConf().getDocument();
                configDocument.reload();

                YamlDocument messagesDocument = plugin.getMessages().getDocument();
                messagesDocument.reload();

                sender.spigot().sendMessage(plugin.getMessages().get(Message.RELOAD_RELOADED_ALL));
            } catch (IOException e) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.RELOAD_ERROR_ALL));
            }
        }
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return RELOADABLE_COMPONENTS;
        }
        return List.of();
    }

}
