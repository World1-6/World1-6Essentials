package com.andrew121410.mc.world16.utils;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.customevents.handlers.AfkEventHandler;
import com.andrew121410.mc.world16.customevents.handlers.UnAfkEventHandler;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.objects.AfkObject;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The Bass API for World1-6Ess
 *
 * @author Andrew121410
 */

public class API {

    private Map<String, UUID> uuidCache;
    private Map<UUID, AfkObject> afkMap;

    private List<String> flyList;
    private List<String> godList;

    private World16Essentials plugin;

    //Finals
    public static final String CUSTOM_COMMAND_FORMAT = "`";
    public static final String DATE_OF_VERSION = "3/22/2021";
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
        if (version.contains("1.16")) {
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

    public UUID getUUIDFromMojangAPI(String playerName) {
        if (uuidCache.get(playerName) != null) return uuidCache.get(playerName);
        URL url;
        UUID uuid1 = null;
        try {
            url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            String uuid = (String) ((JSONObject) new JSONParser()
                    .parse(new InputStreamReader(url.openStream()))).get("id");
            uuid1 = UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-"
                    + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
        uuidCache.put(playerName, uuid1);
        return uuid1;
    }

    public Location getLocationFromFile(CustomYmlManager configinstance, String path) {
        if (configinstance == null || path == null) return null;
        return (Location) configinstance.getConfig().get(path);
    }

    public void setLocationToFile(CustomYmlManager configinstance, String path, Location location) {
        if (configinstance == null || path == null || location == null) {
            return;
        }
        configinstance.getConfig().set(path, location);
        configinstance.saveConfig();
    }

    public Block getBlockPlayerIsLookingAt(Player player) {
        return player.getTargetBlock(null, 5);
    }

    public ConfigurationSection getPlayersYML(CustomConfigManager customConfigManager, Player player) {
        ConfigurationSection configurationSection = customConfigManager.getPlayersYml().getConfig().getConfigurationSection("UUID." + player.getUniqueId());
        if (configurationSection == null)
            configurationSection = customConfigManager.getPlayersYml().getConfig().createSection("UUID." + player.getUniqueId());
        return configurationSection;
    }

    public void doAfk(Player player, String color) {
        if (afkMap.get(player.getUniqueId()).isAfk()) {
            this.plugin.getServer().broadcastMessage(Translate.chat("&7*" + color + " " + player.getDisplayName() + "&r&7 is no longer AFK."));
            this.afkMap.get(player.getUniqueId()).restart(player);
            new UnAfkEventHandler(this.plugin, player.getDisplayName());
        } else if (!afkMap.get(player.getUniqueId()).isAfk()) {
            this.plugin.getServer().broadcastMessage(Translate.chat("&7* " + color + player.getDisplayName() + "&r&7" + " is now AFK."));
            this.afkMap.get(player.getUniqueId()).setAfk(true, player.getLocation());
            new AfkEventHandler(this.plugin, player.getDisplayName()); //CALLS THE EVENT.
        }
    }

    public boolean isClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void permissionErrorMessage(Player p) {
        p.sendMessage(Translate.chat("&4You do not have permission to do this command."));
    }
}