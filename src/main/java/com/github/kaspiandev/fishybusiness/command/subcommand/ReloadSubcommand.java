package com.github.kaspiandev.fishybusiness.command.subcommand;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.command.SubCommand;
import com.github.kaspiandev.fishybusiness.command.SubCommands;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadSubcommand extends SubCommand {

    private final FishyBusiness plugin;

    public ReloadSubcommand(FishyBusiness plugin) {
        super(plugin, SubCommands.RELOAD);
        this.plugin = plugin;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
    }

    @Override
    public List<String> suggestions(CommandSender sender, String[] args) {
        return List.of();
    }

}
