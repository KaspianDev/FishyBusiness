package com.github.kaspiandev.fishybusiness.hook.worldguard;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.hook.Hook;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

public class PlaceholderAPIHook extends Hook<PlaceholderAPIPlugin> {

    public PlaceholderAPIHook(FishyBusiness plugin) {
        super(plugin, "PlaceholderAPI");
        new Expansion().register();
    }

    @Override
    protected void load() {}

    public String applyPlaceholders(Player player, String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    public class Expansion extends PlaceholderExpansion {

        @NotNull
        @Override
        public String getIdentifier() {
            return "fishybusiness";
        }

        @NotNull
        @Override
        public String getAuthor() {
            return plugin.getDescription().getAuthors().toString();
        }

        @NotNull
        @Override
        public String getVersion() {
            return plugin.getDescription().getVersion();
        }

        @Nullable
        @Override
        public String onPlaceholderRequest(Player player, @NotNull String params) {
            if (plugin.getPointManager().isPresent()) {
                if (params.equals("points")) {
                    return String.valueOf(plugin.getPointManager().get().getPoints(player));
                } else if (params.equals("top_points")) {
                    StringJoiner joiner = new StringJoiner("\n");
                    for (Map.Entry<UUID, Integer> entry : plugin.getPointManager().get().getTopPoints().entrySet()) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
                        joiner.add(offlinePlayer.getName() + " " + entry.getValue());
                    }
                    return joiner.toString();
                }
            }

            return null;
        }

    }

}
