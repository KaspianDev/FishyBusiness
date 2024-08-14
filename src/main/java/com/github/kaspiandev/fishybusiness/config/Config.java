package com.github.kaspiandev.fishybusiness.config;

import com.github.kaspiandev.fishybusiness.FishyBusiness;
import com.github.kaspiandev.fishybusiness.exception.PluginLoadFailureException;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
    }

    public YamlDocument getDocument() {
        return document;
    }

}