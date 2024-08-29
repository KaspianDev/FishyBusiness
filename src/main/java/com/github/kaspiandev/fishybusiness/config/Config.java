package com.github.kaspiandev.fishybusiness.config;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.area.AreaTypeRegistry;
import com.github.kaspiandev.fishybusiness.exception.PluginLoadFailureException;
import com.github.kaspiandev.fishybusiness.hook.HookRegistry;
import com.github.kaspiandev.fishybusiness.reward.Reward;
import com.github.kaspiandev.fishybusiness.reward.RewardTypeRegistry;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class Config {

    private final FishyBusiness plugin;
    private final YamlDocument document;

    public Config(FishyBusiness plugin) throws PluginLoadFailureException {
        this.plugin = plugin;
        InputStream defaults = plugin.getResource("config.yml");
        if (defaults == null) {
            throw new PluginLoadFailureException("Defaults do not exist. Jar might be corrupted!");
        }
        try {
            this.document = YamlDocument.create(
                    new File(plugin.getDataFolder(), "config.yml"),
                    defaults,
                    GeneralSettings.builder().setUseDefaults(false).build(),
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    UpdaterSettings.builder().setAutoSave(true).setVersioning(new BasicVersioning("version")).build()
            );
        } catch (IOException ex) {
            throw new PluginLoadFailureException("Config could not be loaded.", ex);
        }

        loadEnabledHooks();
        loadAreaEnabledAdapters();
        loadRewardEnabledAdapters();
    }

    public void loadAreaEnabledAdapters() {
        document.getStringList("area.enabled-adapters").forEach((adapterName) -> {
            AreaTypeRegistry.findByName(adapterName).ifPresent(plugin.getAreaAdapter()::register);
        });
    }

    public void loadRewardEnabledAdapters() {
        document.getStringList("rewards.enabled-adapters").forEach((adapterName) -> {
            RewardTypeRegistry.findByName(adapterName).ifPresent(plugin.getRewardAdapter()::register);
        });
    }

    public void loadEnabledHooks() {
        document.getStringList("hooks.enabled").forEach((hookName) -> {
            HookRegistry.findByName(hookName).ifPresent((hookFunction) -> plugin.getHookManager().register(hookName, hookFunction));
        });
    }

    public boolean isPointsEnabled() {
        return document.getBoolean("points.enabled");
    }

    public boolean isPOBoxItemCustomIcon() {
        return document.getBoolean("hooks.pobox.item.custom-icon");
    }

    public boolean isPOBoxItemCustomName() {
        return document.getBoolean("hooks.pobox.item.custom-name");
    }

    public String getPOBoxItemName() {
        return document.getString("hooks.pobox.item.name");
    }

    public Optional<Section> getRewardDisplay(Class<? extends Reward> clazz) {
        return RewardTypeRegistry.findByClass(clazz)
                                 .map((name) -> document.getSection("rewards.displays." + name));
    }

    public YamlDocument getDocument() {
        return document;
    }

}
