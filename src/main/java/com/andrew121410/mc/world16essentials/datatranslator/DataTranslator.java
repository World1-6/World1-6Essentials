package com.andrew121410.mc.world16essentials.datatranslator;

import com.Zrips.CMI.CMI;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.datatranslator.cmi.CMIDataTranslator;
import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class DataTranslator {

    private final World16Essentials plugin;

    public DataTranslator(World16Essentials plugin) {
        this.plugin = plugin;
    }

    public void convertFrom(Player player, Software software) {
        IDataTranslator iDataTranslator = getDataTranslator(software);

        Instant start = Instant.now();

        iDataTranslator.convertFrom(player);

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        Bukkit.broadcastMessage("World1-6DataTranslator: converted from " + software.name() + " took " + timeElapsed + "Ms");
    }

    public void convertTo(Player player, Software software) {
        IDataTranslator iDataTranslator = getDataTranslator(software);

        Instant start = Instant.now();

        iDataTranslator.convertTo(player);

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        Bukkit.broadcastMessage("World1-6DataTranslator: converted to " + software.name() + " took " + timeElapsed + "Ms");
    }

    private IDataTranslator getDataTranslator(Software software) {
        if (software == Software.ESSENTIALS_X) {
            Essentials essentials = this.plugin.getOtherPlugins().getEssentials();

            if (essentials == null) {
                throw new IllegalStateException("DataTranslator: Essentials plugin must be loaded to convert to/from EssentialsX");
            }

//            return new EssentialsXDataTranslator(this.plugin, essentials);
            return null;
        } else if (software == Software.CMI) {
            CMI cmi = this.plugin.getOtherPlugins().getCmi();

            if (cmi == null) {
                throw new IllegalStateException("DataTranslator: CMI plugin must be loaded to convert to/from CMI");
            }

            return new CMIDataTranslator(this.plugin, cmi);
        } else {
            throw new IllegalArgumentException("Unknown software: " + software.name());
        }
    }

    public static FileConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}
