package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class MessagesUtils {

    private final World16Essentials plugin;
    private final CustomYmlManager messagesYml;

    private String prefix;
    private String welcomeBackMessage;
    private String firstJoinedMessage;
    private String leaveMessage;

    public MessagesUtils(World16Essentials plugin, CustomYmlManager messagesYml) {
        this.plugin = plugin;
        this.messagesYml = messagesYml;

        // Add default messages if they don't exist.
        addDefaults();

        this.prefix = this.messagesYml.getConfig().getString("prefix");
        this.welcomeBackMessage = this.messagesYml.getConfig().getString("welcomeBackMessage");
        this.firstJoinedMessage = this.messagesYml.getConfig().getString("firstJoinedMessage");
        this.leaveMessage = this.messagesYml.getConfig().getString("leaveMessage");
    }

    private void addDefaults() {
        this.messagesYml.getConfig().addDefault("prefix", "[<blue>World1-6<r>]");
        this.messagesYml.getConfig().addDefault("welcomeBackMessage", "%prefix% <gold>Welcome back, %player%!");
        this.messagesYml.getConfig().addDefault("firstJoinedMessage", "%prefix% <gold>Welcome to the server, %player%!");
        this.messagesYml.getConfig().addDefault("leaveMessage", "%prefix% <gold>%player% has left the server.");

        this.messagesYml.getConfig().options().copyDefaults(true);
        this.messagesYml.saveConfig();
        this.messagesYml.reloadConfig();
    }

    public Component parseMessage(Player player, String message) {
        message = message.replaceAll("%player%", player.getName());
        message = message.replaceAll("%prefix%", this.prefix);
        return Translate.miniMessage(message);
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.messagesYml.getConfig().set("prefix", prefix);
        this.messagesYml.saveConfig();
    }

    public String getWelcomeBackMessage() {
        return welcomeBackMessage;
    }

    public void setWelcomeBackMessage(String welcomeBackMessage) {
        this.welcomeBackMessage = welcomeBackMessage;
        this.messagesYml.getConfig().set("welcomeBackMessage", welcomeBackMessage);
        this.messagesYml.saveConfig();
    }

    public String getFirstJoinedMessage() {
        return firstJoinedMessage;
    }

    public void setFirstJoinedMessage(String firstJoinedMessage) {
        this.firstJoinedMessage = firstJoinedMessage;
        this.messagesYml.getConfig().set("firstJoinedMessage", firstJoinedMessage);
        this.messagesYml.saveConfig();
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
        this.messagesYml.getConfig().set("leaveMessage", leaveMessage);
        this.messagesYml.saveConfig();
    }
}