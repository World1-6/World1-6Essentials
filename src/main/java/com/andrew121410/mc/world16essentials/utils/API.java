package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import com.andrew121410.mc.world16utils.utils.ccutils.utils.StringDataTimeBuilder;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class API {

    public static final String CUSTOM_COMMAND_FORMAT = "`";
    public static final String DATE_OF_VERSION = "6/24/2022";

    private final World16Essentials plugin;

    // Config
    private boolean signTranslateColors;
    private boolean preventCropsTrampling;
    private int spawnMobCap;

    // Messages
    private String prefix;
    private String welcomeBackMessage;
    private String firstJoinedMessage;
    private String leaveMessage;

    private final Map<UUID, Long> timeOfLoginMap;
    private final Map<UUID, AfkObject> afkMap;

    private final List<String> flyList;
    private final List<String> godList;
    private final List<Player> hiddenPlayers;

    public API(World16Essentials plugin) {
        this.plugin = plugin;

        // Config
        this.signTranslateColors = this.plugin.getConfig().getBoolean("signTranslateColors");
        this.preventCropsTrampling = this.plugin.getConfig().getBoolean("preventCropsTrampling");
        this.spawnMobCap = this.plugin.getConfig().getInt("spawnMobCap");

        // Messages
        this.prefix = this.plugin.getCustomConfigManager().getMessagesYml().getConfig().getString("prefix");
        this.welcomeBackMessage = this.plugin.getCustomConfigManager().getMessagesYml().getConfig().getString("welcomeBackMessage");
        this.firstJoinedMessage = this.plugin.getCustomConfigManager().getMessagesYml().getConfig().getString("firstJoinedMessage");
        this.leaveMessage = this.plugin.getCustomConfigManager().getMessagesYml().getConfig().getString("leaveMessage");

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
        return StringDataTimeBuilder.makeIntoEnglishWords(loginTime, System.currentTimeMillis(), false);
    }

    public String getTimeSinceLastLogin(OfflinePlayer offlinePlayer) {
        long lastPlayed = offlinePlayer.getLastPlayed();
        return StringDataTimeBuilder.makeIntoEnglishWords(lastPlayed, System.currentTimeMillis(), false);
    }

    public String getTimeSinceFirstLogin(OfflinePlayer offlinePlayer) {
        long firstPlayed = offlinePlayer.getFirstPlayed();
        return StringDataTimeBuilder.makeIntoEnglishWords(firstPlayed, System.currentTimeMillis(), false);
    }

    public boolean didPlayerJustJoin(Player player) {
        long loginTime = timeOfLoginMap.get(player.getUniqueId());
        long timeElapsed = System.currentTimeMillis() - loginTime;
        long minutes = (timeElapsed % (1000 * 60 * 60)) / (1000 * 60);
        return minutes < 1;
    }

    // Config
    public boolean isSignTranslateColors() {
        return signTranslateColors;
    }

    public void setSignTranslateColors(boolean signTranslateColors) {
        this.signTranslateColors = signTranslateColors;
        this.plugin.getConfig().set("signTranslateColors", signTranslateColors);
        this.plugin.saveConfig();
    }

    public boolean isPreventCropsTrampling() {
        return preventCropsTrampling;
    }

    public void setPreventCropsTrampling(boolean preventCropsTrampling) {
        this.preventCropsTrampling = preventCropsTrampling;
        this.plugin.getConfig().set("preventCropsTrampling", preventCropsTrampling);
        this.plugin.saveConfig();
    }

    public int getSpawnMobCap() {
        return spawnMobCap;
    }

    public void setSpawnMobCap(int spawnMobCap) {
        this.spawnMobCap = spawnMobCap;
        this.plugin.getConfig().set("spawnMobCap", spawnMobCap);
        this.plugin.saveConfig();
    }

    // Messages
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.plugin.getCustomConfigManager().getMessagesYml().getConfig().set("prefix", prefix);
        this.plugin.getCustomConfigManager().getMessagesYml().saveConfig();
    }

    public String getWelcomeBackMessage() {
        return welcomeBackMessage;
    }

    public void setWelcomeBackMessage(String welcomeBackMessage) {
        this.welcomeBackMessage = welcomeBackMessage;
        this.plugin.getCustomConfigManager().getMessagesYml().getConfig().set("welcomeBackMessage", welcomeBackMessage);
        this.plugin.getCustomConfigManager().getMessagesYml().saveConfig();
    }

    public String getFirstJoinedMessage() {
        return firstJoinedMessage;
    }

    public void setFirstJoinedMessage(String firstJoinedMessage) {
        this.firstJoinedMessage = firstJoinedMessage;
        this.plugin.getCustomConfigManager().getMessagesYml().getConfig().set("firstJoinedMessage", firstJoinedMessage);
        this.plugin.getCustomConfigManager().getMessagesYml().saveConfig();
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
        this.plugin.getCustomConfigManager().getMessagesYml().getConfig().set("leaveMessage", leaveMessage);
        this.plugin.getCustomConfigManager().getMessagesYml().saveConfig();
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

    public ConfigurationSection getPlayersYML(CustomConfigManager customConfigManager, Player player) {
        ConfigurationSection configurationSection = customConfigManager.getPlayersYml().getConfig().getConfigurationSection("UUID." + player.getUniqueId());
        if (configurationSection == null)
            configurationSection = customConfigManager.getPlayersYml().getConfig().createSection("UUID." + player.getUniqueId());
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

    public String parseMessage(Player player, String message) {
        message = message.replaceAll("%player%", player.getDisplayName());
        message = message.replaceAll("%prefix%", this.prefix);
        return Translate.color(message);
    }

    public void sendPermissionErrorMessage(Player player) {
        player.sendMessage(Translate.chat("&4You do not have permission to do this command."));
    }
}