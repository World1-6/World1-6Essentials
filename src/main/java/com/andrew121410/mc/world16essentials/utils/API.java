package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import com.andrew121410.mc.world16utils.utils.ccutils.utils.StringDataTimeBuilder;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class API {

    public static final String DATE_OF_VERSION = "11/18/2022";
    public static final String CUSTOM_COMMAND_FORMAT = "`";

    private final World16Essentials plugin;

    // Configuration Utils
    private final ConfigUtils configUtils;
    private final MessagesUtils messagesUtils;

    private final Map<UUID, Long> timeOfLoginMap;
    private final Map<UUID, AfkObject> afkMap;

    private final List<String> flyList;
    private final List<String> godList;
    private final List<Player> hiddenPlayers;

    public API(World16Essentials plugin) {
        this.plugin = plugin;

        // Configuration Utils
        this.configUtils = this.plugin.getCustomConfigManager().getConfigUtils();
        this.messagesUtils = this.plugin.getCustomConfigManager().getMessagesUtils();

        this.timeOfLoginMap = this.plugin.getSetListMap().getTimeOfLoginMap();
        this.afkMap = this.plugin.getSetListMap().getAfkMap();

        this.flyList = this.plugin.getSetListMap().getFlyList();
        this.godList = this.plugin.getSetListMap().getGodList();
        this.hiddenPlayers = this.plugin.getSetListMap().getHiddenPlayers();
    }

    public boolean isAfk(Player player) {
        return afkMap.get(player.getUniqueId()).isAfk();
    }

    public boolean isFlying(Player player) {
        return flyList.contains(player.getDisplayName()) || player.isFlying();
    }

    public boolean isGod(Player player) {
        return godList.contains(player.getDisplayName());
    }

    public boolean isHidden(Player player) {
        return hiddenPlayers.contains(player);
    }

    public String getTimeSinceLogin(Player player) {
        long loginTime = timeOfLoginMap.get(player.getUniqueId());
        return StringDataTimeBuilder.makeIntoEnglishWords(loginTime, System.currentTimeMillis(), false, false);
    }

    public String getTimeSinceLastLogin(OfflinePlayer offlinePlayer) {
        long lastLogin = offlinePlayer.getLastLogin();
        return StringDataTimeBuilder.makeIntoEnglishWords(lastLogin, System.currentTimeMillis(), false, false);
    }

    public String getTimeSinceFirstLogin(OfflinePlayer offlinePlayer) {
        long firstPlayed = offlinePlayer.getFirstPlayed();
        return StringDataTimeBuilder.makeIntoEnglishWords(firstPlayed, System.currentTimeMillis(), false, false);
    }

    public boolean didPlayerJustJoin(Player player) {
        long loginTime = timeOfLoginMap.get(player.getUniqueId());
        long timeElapsed = System.currentTimeMillis() - loginTime;
        long minutes = (timeElapsed % (1000 * 60 * 60)) / (1000 * 60);
        return minutes < 1;
    }

    public Location getLocationFromFile(CustomYmlManager customYmlManager, String path) {
        if (customYmlManager == null || path == null) return null;
        return (Location) customYmlManager.getConfig().get(path);
    }

    public void setLocationToFile(CustomYmlManager customYmlManager, String path, Location location) {
        if (customYmlManager == null || path == null || location == null) {
            return;
        }
        customYmlManager.getConfig().set(path, location);
        customYmlManager.saveConfig();
    }

    public ConfigurationSection getPlayersYML(Player player) {
        ConfigurationSection configurationSection = this.plugin.getCustomConfigManager().getPlayersYml().getConfig().getConfigurationSection("UUID." + player.getUniqueId());
        if (configurationSection == null)
            configurationSection = this.plugin.getCustomConfigManager().getPlayersYml().getConfig().createSection("UUID." + player.getUniqueId());
        return configurationSection;
    }

    public void doAfk(Player player, String color) {
        AfkObject afkObject = this.afkMap.get(player.getUniqueId());
        if (afkObject.isAfk()) {
            this.plugin.getServer().broadcastMessage(Translate.chat("&7*" + color + " " + player.getDisplayName() + "&r&7 is no longer AFK."));
            afkObject.restart(player);
        } else {
            this.plugin.getServer().broadcastMessage(Translate.chat("&7* " + color + player.getDisplayName() + "&r&7" + " is now AFK."));
            afkObject.setAfk(true, player.getLocation());
        }
    }

    public void sendPermissionErrorMessage(CommandSender sender) {
        sender.sendMessage(Translate.chat("&4You do not have permission to do this command."));
    }

    public String parseMessage(Player player, String message) {
        return this.getMessagesUtils().parseMessage(player, message);
    }

    public ConfigUtils getConfigUtils() {
        return configUtils;
    }

    public MessagesUtils getMessagesUtils() {
        return messagesUtils;
    }
}