package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class API {

    public static final String CUSTOM_COMMAND_FORMAT = "`";
    public static final String DATE_OF_VERSION = "6/10/2022";

    private final String prefix;
    private final Map<UUID, Long> timeOfLoginMap;
    private Map<UUID, AfkObject> afkMap;
    private List<String> flyList;
    private List<String> godList;
    private final World16Essentials plugin;

    public API(World16Essentials plugin) {
        this.plugin = plugin;
        this.prefix = this.plugin.getConfig().getString("prefix");

        this.timeOfLoginMap = this.plugin.getSetListMap().getTimeOfLoginMap();

        this.afkMap = this.plugin.getSetListMap().getAfkMap();
        this.flyList = this.plugin.getSetListMap().getFlyList();
        this.godList = this.plugin.getSetListMap().getGodList();
    }

    public boolean isAfk(Player p) {
        return afkMap.get(p.getUniqueId()).isAfk();
    }

    public boolean isFlying(Player p) {
        return flyList.contains(p.getDisplayName()) || p.isFlying();
    }

    public boolean isGod(Player p) {
        return godList.contains(p.getDisplayName());
    }

    public boolean isDebug() {
        return plugin.getConfig().getString("debug").equalsIgnoreCase("true");
    }

    public String getTimeSinceLogin(Player player) {
        long loginTime = timeOfLoginMap.get(player.getUniqueId());
        return StringDataTimeBuilder.makeString(loginTime, System.currentTimeMillis());
    }

    public String getTimeSinceLastLogin(OfflinePlayer offlinePlayer) {
        long lastPlayed = offlinePlayer.getLastPlayed();
        return StringDataTimeBuilder.makeString(lastPlayed, System.currentTimeMillis());
    }

    public String getTimeSinceFirstLogin(OfflinePlayer offlinePlayer) {
        long firstPlayed = offlinePlayer.getFirstPlayed();
        return StringDataTimeBuilder.makeString(firstPlayed, System.currentTimeMillis());
    }

    public boolean didPlayerJustJoin(Player player) {
        long loginTime = timeOfLoginMap.get(player.getUniqueId());
        long timeElapsed = System.currentTimeMillis() - loginTime;
        long minutes = (timeElapsed % (1000 * 60 * 60)) / (1000 * 60);
        return minutes < 1;
    }

    public boolean isSignTranslateColors() {
        return plugin.getConfig().getString("signTranslateColors").equalsIgnoreCase("true");
    }

    public boolean isPreventCropsTrampling() {
        return plugin.getConfig().getString("preventCropsTrampling").equalsIgnoreCase("true");
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

    public void sendPermissionErrorMessage(Player player) {
        player.sendMessage(Translate.chat("&4You do not have permission to do this command."));
    }

    public String getPrefix() {
        return prefix;
    }
}