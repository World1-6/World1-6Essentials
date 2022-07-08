package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.configuration.file.FileConfiguration;

public class MessagesUtils {

    private final World16Essentials plugin;
    private final CustomYmlManager messagesYml;
    private final FileConfiguration messagesConfig;

    private String prefix;
    private String welcomeBackMessage;
    private String firstJoinedMessage;
    private String leaveMessage;
    private String bedMessage;

    public MessagesUtils(World16Essentials plugin, CustomYmlManager messagesYml) {
        this.plugin = plugin;
        this.messagesYml = messagesYml;
        this.messagesConfig = messagesYml.getConfig();

        // Defaults
        this.messagesConfig.addDefault("prefix", "[&9World1-6&r]");
        this.messagesConfig.addDefault("welcomeBackMessage", "%prefix% &6Welcome back, %player%!");
        this.messagesConfig.addDefault("firstJoinedMessage", "%prefix% &6Welcome to the server, %player%!");
        this.messagesConfig.addDefault("leaveMessage", "%prefix% &6%player% has left the server.");
        this.messagesConfig.addDefault("bedMessage", "%prefix% &6%player% has slept.");

        this.messagesConfig.options().copyDefaults(true);
        this.messagesYml.saveConfig();
        this.messagesYml.reloadConfig();

        // Load
        this.prefix = this.messagesConfig.getString("prefix");
        this.welcomeBackMessage = this.messagesConfig.getString("welcomeBackMessage");
        this.firstJoinedMessage = this.messagesConfig.getString("firstJoinedMessage");
        this.leaveMessage = this.messagesConfig.getString("leaveMessage");
        this.bedMessage = this.messagesConfig.getString("bedMessage");
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.messagesConfig.set("prefix", prefix);
        this.messagesYml.saveConfig();
    }

    public String getWelcomeBackMessage() {
        return welcomeBackMessage;
    }

    public void setWelcomeBackMessage(String welcomeBackMessage) {
        this.welcomeBackMessage = welcomeBackMessage;
        this.messagesConfig.set("welcomeBackMessage", welcomeBackMessage);
        this.messagesYml.saveConfig();
    }

    public String getFirstJoinedMessage() {
        return firstJoinedMessage;
    }

    public void setFirstJoinedMessage(String firstJoinedMessage) {
        this.firstJoinedMessage = firstJoinedMessage;
        this.messagesConfig.set("firstJoinedMessage", firstJoinedMessage);
        this.messagesYml.saveConfig();
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
        this.messagesConfig.set("leaveMessage", leaveMessage);
        this.messagesYml.saveConfig();
    }

    public String getBedMessage() {
        return bedMessage;
    }

    public void setBedMessage(String bedMessage) {
        this.bedMessage = bedMessage;
        this.messagesConfig.set("bedMessage", bedMessage);
        this.messagesYml.saveConfig();
    }
}