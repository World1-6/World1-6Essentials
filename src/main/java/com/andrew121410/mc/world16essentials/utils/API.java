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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class API {

    private Map<String, UUID> uuidCache;
    private Map<UUID, AfkObject> afkMap;

    private List<String> flyList;
    private List<String> godList;

    private World16Essentials plugin;

    //Finals
    public static final String CUSTOM_COMMAND_FORMAT = "`";
    public static final String DATE_OF_VERSION = "12/16/2021";
    public static final String PREFIX = "[&9World1-6Ess&r]";

    public API(World16Essentials plugin) {
        this.plugin = plugin;
        doSetListMap();
    }

    private void doSetListMap() {
        this.uuidCache = this.plugin.getSetListMap().getUuidCache();
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

    public boolean isSignTranslateColors() {
        return plugin.getConfig().getString("signTranslateColors").equalsIgnoreCase("true");
    }

    public boolean isPreventCropsTrampling() {
        return plugin.getConfig().getString("preventCropsTrampling").equalsIgnoreCase("true");
    }

    public String getTimeFormattedString() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy - hh:mm:ss a z");
        return zonedDateTime.format(myFormatObj);
    }

    public String getPlayerLastOnlineDateFormattedString(OfflinePlayer target) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - hh:mm a z");
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(target.getLastPlayed()).atZone(ZoneId.systemDefault());
        return zonedDateTime.format(formatter);
    }

    public String getServerVersion() {
        String version = this.plugin.getServer().getVersion();
        if (version.contains("1.18")) {
            return "1.18";
        } else if (version.contains("1.17")) {
            return "1.17";
        } else if (version.contains("1.16")) {
            return "1.16";
        } else if (version.contains("1.15")) {
            return "1.15";
        } else if (version.contains("1.14")) {
            return "1.14";
        } else if (version.contains("1.13")) {
            return "1.13";
        } else if (version.contains("1.12")) {
            return "1.12";
        }
        return null;
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
}