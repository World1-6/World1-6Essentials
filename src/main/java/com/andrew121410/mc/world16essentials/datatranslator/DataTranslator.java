package com.andrew121410.mc.world16essentials.datatranslator;

import com.andrew121410.mc.world16essentials.World16Essentials;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class DataTranslator {

    private final World16Essentials plugin;

    public DataTranslator(World16Essentials plugin) {
        this.plugin = plugin;
    }

    public void convertFrom(Software software) {
        IDataTranslator iDataTranslator = getDataTranslator(software);
        if (iDataTranslator == null) return;

        Instant start = Instant.now();

        iDataTranslator.convertFrom();

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        Bukkit.broadcastMessage("World1-6DataTranslator: converted from " + software.name() + " took " + timeElapsed + "Ms");
    }

    public void convertTo(Software software) {
        IDataTranslator iDataTranslator = getDataTranslator(software);
        if (iDataTranslator == null) return;

        Instant start = Instant.now();

        iDataTranslator.convertTo();

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        Bukkit.broadcastMessage("World1-6DataTranslator: converted to " + software.name() + " took " + timeElapsed + "Ms");
    }

    private IDataTranslator getDataTranslator(Software software) {
        if (software == Software.ESSENTIALS_X) {
            if (hasEssentialsXPlugin()) {
                return new EssentialsXDataTranslator(plugin);
            } else {
                throw new IllegalArgumentException("EssentialsX plugin not found!");
            }
        }
        return null;
    }

    private boolean hasEssentialsXPlugin() {
        return Bukkit.getPluginManager().getPlugin("Essentials") != null;
    }

    public static FileConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}