package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;

public class CustomConfigManager {

    private final World16Essentials plugin;

    private CustomYmlManager shitYml;
    private CustomYmlManager warpsYml;
    private CustomYmlManager playersYml;
    private CustomYmlManager messagesYml;

    public CustomConfigManager(World16Essentials plugin) {
        this.plugin = plugin;
    }

    public void registerAllCustomConfigs() {
        //shit.yml
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

        //messages.yml
        this.messagesYml = new CustomYmlManager(this.plugin, false);
        this.messagesYml.setup("messages.yml");
        setupDefaultsForMessages();
        this.messagesYml.saveConfig();
        this.messagesYml.reloadConfig();
        //...
    }

    private void setupDefaultsForMessages() {
        this.messagesYml.getConfig().addDefault("prefix", "[&9World1-6&r]");
        this.messagesYml.getConfig().addDefault("welcomeBackMessage", "%prefix% &6Welcome back, %player%!");
        this.messagesYml.getConfig().addDefault("firstJoinedMessage", "%prefix% &6Welcome to the server, %player%!");
        this.messagesYml.getConfig().addDefault("leaveMessage", "%prefix% &6%player% has left the server.");

        this.messagesYml.getConfig().options().copyDefaults(true);
    }

    public void saveAll() {
        this.plugin.saveConfig();
        this.shitYml.saveConfig();
        this.warpsYml.saveConfig();
        this.playersYml.saveConfig();
        this.messagesYml.saveConfig();
    }

    public void reloadAll() {
        this.plugin.reloadConfig();
        this.shitYml.reloadConfig();
        this.warpsYml.reloadConfig();
        this.playersYml.reloadConfig();
        this.messagesYml.reloadConfig();
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

    public CustomYmlManager getMessagesYml() {
        return messagesYml;
    }
}
