package com.github.kaspiandev.fishybusiness.hook;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.config.Message;
import com.github.kaspiandev.fishybusiness.points.TopPointEntry;
import com.github.kaspiandev.fishybusiness.util.ComponentUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.StringJoiner;

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
                    List<TopPointEntry> topEntries = plugin.getPointManager().get().getTopPoints();
                    StringJoiner joiner = new StringJoiner("\n");
                    for (int i = 0; i < topEntries.size(); i++) {
                        TopPointEntry entry = topEntries.get(i);
                        String name = Bukkit.getOfflinePlayer(entry.uuid()).getName();

                        int currentPlace = i + 1;
                        String topEntryFormat = ComponentUtil.toString(plugin.getMessages().get(Message.POINTS_TOP_LIST_ENTRY, (format) -> {
                            return format.replace("${name}", (name == null) ? "unknown" : name)
                                         .replace("${points}", String.valueOf(entry.amount()))
                                         .replace("${place}", String.valueOf(currentPlace));
                        }));
                        joiner.add(topEntryFormat);
                    }
                    return joiner.toString();
                } else if (params.startsWith("top_points_")) {
                    try {
                        int place = Math.min(9, Integer.parseInt(params.substring("top_points_".length())) - 1);
                        TopPointEntry entry = plugin.getPointManager().get().getTopPoints().get(place);

                        String name = Bukkit.getOfflinePlayer(entry.uuid()).getName();
                        return ComponentUtil.toString(plugin.getMessages().get(Message.POINTS_TOP_ENTRY, (format) -> {
                            return format.replace("${name}", (name == null) ? "unknown" : name)
                                         .replace("${points}", String.valueOf(entry.amount()));
                        }));
                    } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                        return null;
                    }
                }
            }
            return null;
        }

    }

}
