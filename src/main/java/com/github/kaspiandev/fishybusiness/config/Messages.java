package com.github.kaspiandev.fishybusiness.config;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.exception.PluginLoadFailureException;
import com.github.kaspiandev.fishybusiness.hook.worldguard.PlaceholderAPIHook;
import com.github.kaspiandev.fishybusiness.util.ColorUtil;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.UnaryOperator;

public class Messages {

    private final FishyBusiness plugin;
    private final YamlDocument document;

    public Messages(FishyBusiness plugin) throws PluginLoadFailureException {
        this.plugin = plugin;
        InputStream defaults = plugin.getResource("messages.yml");
        if (defaults == null) {
            throw new PluginLoadFailureException("Defaults do not exist. Jar might be corrupted!");
        }
        try {
            this.document = YamlDocument.create(
                    new File(plugin.getDataFolder(), "messages.yml"),
                    defaults,
                    GeneralSettings.builder().setUseDefaults(false).build(),
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    UpdaterSettings.builder().setAutoSave(true).setVersioning(new BasicVersioning("version")).build()
            );
        } catch (IOException ex) {
            throw new PluginLoadFailureException("Config could not be loaded.", ex);
        }
    }

    public BaseComponent[] get(Message message) {
        return ColorUtil.component(document.getString(message.getPath()));
    }

    public BaseComponent[] get(Message message, Player player) {
        String configMessage = document.getString(message.getPath());
        return ColorUtil.component(plugin.getHookManager().findHook(PlaceholderAPIHook.class)
                                         .map((hook) -> hook.applyPlaceholders(player, configMessage))
                                         .orElse(configMessage));
    }

    public BaseComponent[] get(Message message, UnaryOperator<String> function) {
        return ColorUtil.component(function.apply(document.getString(message.getPath())));
    }

    public BaseComponent[] get(Message message, UnaryOperator<String> function, Player player) {
        String configMessage = function.apply(document.getString(message.getPath()));
        return ColorUtil.component(plugin.getHookManager().findHook(PlaceholderAPIHook.class)
                                         .map((hook) -> hook.applyPlaceholders(player, configMessage))
                                         .orElse(configMessage));
    }

    public YamlDocument getDocument() {
        return document;
    }

}
