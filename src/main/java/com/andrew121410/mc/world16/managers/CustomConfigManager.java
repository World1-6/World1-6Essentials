package com.andrew121410.mc.world16.managers;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;

public class CustomConfigManager {

    private Main plugin;

    private CustomYmlManager shitYml;
    private CustomYmlManager warpsYml;
    private CustomYmlManager playersYml;

    public CustomConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void registerAllCustomConfigs() {
        //Shit.yml
        this.shitYml = new CustomYmlManager(this.plugin, false);
        this.shitYml.setup("shit.yml");
        this.shitYml.saveConfig();
        this.shitYml.reloadConfig();
        //...

        //warps.yml
        this.warpsYml = new CustomYmlManager(this.plugin, false);
        this.warpsYml.setup("warps.yml");
        this.warpsYml.saveConfig();
        this.warpsYml.reloadConfig();
        //...

        //players.yml
        this.playersYml = new CustomYmlManager(this.plugin, false);
        this.playersYml.setup("players.yml");
        this.playersYml.saveConfig();
        this.playersYml.reloadConfig();
        //...
    }

    public void saveAll() {
        this.shitYml.saveConfig();
        this.warpsYml.saveConfig();
        this.playersYml.saveConfig();
    }

    public void reloadAll() {
        this.plugin.reloadConfig();
        this.shitYml.reloadConfig();
        this.warpsYml.reloadConfig();
        this.playersYml.reloadConfig();
    }

    public CustomYmlManager getShitYml() {
        return shitYml;
    }

    public CustomYmlManager getWarpsYml() {
        return warpsYml;
    }

    public CustomYmlManager getPlayersYml() {
        return playersYml;
    }
}
