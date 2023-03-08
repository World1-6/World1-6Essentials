package com.andrew121410.mc.world16essentials.datatranslator;

import com.Zrips.CMI.CMI;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.datatranslator.cmi.CMIDataTranslator;
import com.andrew121410.mc.world16essentials.datatranslator.essentialsx.EssentialsXDataTranslator;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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
        IDataTranslator iDataTranslator = getDataTranslator(player, software);
        if (iDataTranslator == null) return;

        // Start timer
        Instant start = Instant.now();

        // Convert
        iDataTranslator.convertFrom(player);

        // End timer
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        if (player != null) {
            player.sendMessage(Translate.miniMessage("<green>Converted from " + software.name() + " took " + timeElapsed + "Ms"));
        } else {
            Bukkit.broadcastMessage("World1-6DataTranslator: converted from " + software.name() + " took " + timeElapsed + "Ms");
        }
    }

    public void convertTo(Player player, Software software) {
        IDataTranslator iDataTranslator = getDataTranslator(player, software);
        if (iDataTranslator == null) return;

        // Start timer
        Instant start = Instant.now();

        // Convert
        iDataTranslator.convertTo(player);

        // End timer
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        if (player != null) {
            player.sendMessage(Translate.miniMessage("<green>Converted to " + software.name() + " took " + timeElapsed + "Ms"));
        } else {
            Bukkit.broadcastMessage("World1-6DataTranslator: converted to " + software.name() + " took " + timeElapsed + "Ms");
        }
    }

    private IDataTranslator getDataTranslator(CommandSender sender, Software software) {
        if (software == Software.ESSENTIALS_X) {
            Essentials essentials = this.plugin.getOtherPlugins().getEssentials();

            if (essentials == null) {
                if (sender != null) {
                    sender.sendMessage(Translate.miniMessage("<red>The plugin EssentialsX must be loaded to convert to/from EssentialsX"));
                    return null;
                } else {
                    throw new IllegalStateException("DataTranslator: EssentialsX plugin must be loaded to convert to/from EssentialsX");
                }
            }

            return new EssentialsXDataTranslator(this.plugin, essentials);
        } else if (software == Software.CMI) {
            CMI cmi = this.plugin.getOtherPlugins().getCmi();

            if (cmi == null) {
                if (sender != null) {
                    sender.sendMessage(Translate.miniMessage("<red>The plugin CMI must be loaded to convert to/from CMI"));
                    return null;
                } else {
                    throw new IllegalStateException("DataTranslator: CMI plugin must be loaded to convert to/from CMI");
                }
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
