package com.github.kaspiandev.fishybusiness.command;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.config.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class FishyBusinessCommand implements TabExecutor {

    private final FishyBusiness plugin;
    private final SubCommandRegistry registry;

    public FishyBusinessCommand(FishyBusiness plugin, SubCommandRegistry registry) {
        this.plugin = plugin;
        this.registry = registry;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        if (args.length < 1) {
            sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_NO_ARGUMENTS));
            return false;
        } else {
            SubCommand cmd = registry.findById(args[0]);
            if (cmd == null) {
                sender.spigot().sendMessage(plugin.getMessages().get(Message.COMMAND_INVALID_SUBCOMMAND));
                return false;
            }
            cmd.checkPerms(sender, args);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
                                      String label, String[] args) {
        if (args.length <= 1) {
            return new ArrayList<>(registry.getRegistry().keySet());
        } else {
            SubCommand subCommand = registry.findById(args[0]);
            if (subCommand == null) return null;

            return subCommand.suggestions(sender, args);
        }
    }

}
