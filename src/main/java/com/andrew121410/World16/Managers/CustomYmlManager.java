package com.andrew121410.World16.Managers;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomYmlManager {

    private Main plugin;

    private FileConfiguration fileConfiguration;
    private File file;

    private String fileName;

    public CustomYmlManager(Main plugin) {
        this.plugin = plugin;
    }

    public void setup(String fileName) {
        this.fileName = fileName;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        file = new File(plugin.getDataFolder(), this.fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(Translate.chat(API.USELESS_TAG + " The {nameoffile} has been created.").replace("{nameoffile}", this.fileName));
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender()
                        .sendMessage(Translate
                                .chat(API.USELESS_TAG + " The {nameoffile} could not make for some reason.").replace("{nameoffile}", this.fileName));
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    public void saveConfig() {
        try {
            fileConfiguration.save(file);
            if (this.plugin.getApi().isDebug()) {
                Bukkit.getServer().getConsoleSender().sendMessage(Translate.chat(API.USELESS_TAG + " &aThe {name} has been saved.").replace("{name}", this.fileName));
            }
        } catch (IOException e) {
            Bukkit.getServer().getConsoleSender()
                    .sendMessage(Translate.chat(API.USELESS_TAG + " &cThe {name} has been NOT SAVED..").replace("{name}", this.fileName));
        }
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (this.plugin.getApi().isDebug()) {
            Bukkit.getServer().getConsoleSender().sendMessage(Translate.chat(API.USELESS_TAG + " &6The {nameoffile} has been reloaded.").replace("{nameoffile}", this.fileName));
        }
    }
}
