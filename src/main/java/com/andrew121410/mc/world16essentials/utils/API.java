package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import com.andrew121410.mc.world16utils.utils.ccutils.utils.TimeUtils;
import com.google.common.io.CharStreams;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class API {

    public String dateOfBuild;

    private final World16Essentials plugin;

    // Configuration Utils
    private final ConfigUtils configUtils;
    private final MessagesUtils messagesUtils;

    private final Map<UUID, Long> timeOfLoginMap;
    private final Map<UUID, AfkObject> afkMap;

    private final List<UUID> godList;
    private final List<UUID> hiddenPlayers;

    public API(World16Essentials plugin) {
        this.plugin = plugin;

        this.dateOfBuild = getDateOfBuildFromTxtFile();

        // Configuration Utils
        this.configUtils = this.plugin.getCustomConfigManager().getConfigUtils();
        this.messagesUtils = this.plugin.getCustomConfigManager().getMessagesUtils();

        this.timeOfLoginMap = this.plugin.getMemoryHolder().getTimeOfLoginMap();
        this.afkMap = this.plugin.getMemoryHolder().getAfkMap();

        this.godList = this.plugin.getMemoryHolder().getGodList();
        this.hiddenPlayers = this.plugin.getMemoryHolder().getHiddenPlayers();
    }

    public boolean isAfk(Player player) {
        return afkMap.get(player.getUniqueId()).isAfk();
    }

    public boolean isGod(Player player) {
        return godList.contains(player.getUniqueId());
    }

    public boolean isHidden(Player player) {
        return hiddenPlayers.contains(player.getUniqueId());
    }

    public String getTimeSinceLogin(Player player) {
        long loginTime = timeOfLoginMap.get(player.getUniqueId());
        return TimeUtils.makeIntoEnglishWords(loginTime, System.currentTimeMillis(), false, true);
    }

    public String getTimeSinceLastLogin(OfflinePlayer offlinePlayer) {
        long lastLogin = offlinePlayer.getLastLogin();
        return TimeUtils.makeIntoEnglishWords(lastLogin, System.currentTimeMillis(), false, true);
    }

    public String getTimeSinceFirstLogin(OfflinePlayer offlinePlayer) {
        long firstPlayed = offlinePlayer.getFirstPlayed();
        return TimeUtils.makeIntoEnglishWords(firstPlayed, System.currentTimeMillis(), false, true);
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
            this.plugin.getServer().broadcastMessage(Translate.color("&7*" + color + " " + player.getDisplayName() + "&r&7 is no longer AFK."));
            afkObject.restart(player);
        } else {
            this.plugin.getServer().broadcastMessage(Translate.color("&7* " + color + player.getDisplayName() + "&r&7" + " is now AFK."));
            afkObject.setAfk(true, player.getLocation());
        }
    }

    public void saveFlyingState(Player player) {
        // Don't save flying state if player is in creative.
        if (player.getGameMode() == GameMode.CREATIVE) return;

        // Don't save if player isn't flying.
        if (!player.isFlying()) return;

        ConfigurationSection configurationSection = getPlayersYML(player);
        configurationSection.set("Flying", true);
        this.plugin.getCustomConfigManager().getPlayersYml().saveConfig();
    }

    public void loadFlyingState(Player player) {
        ConfigurationSection configurationSection = getPlayersYML(player);
        if (configurationSection.get("Flying") == null) return;
        boolean fly = configurationSection.getBoolean("Flying");
        player.setAllowFlight(fly);
        player.setFlying(fly);

        configurationSection.set("Flying", null);
        this.plugin.getCustomConfigManager().getPlayersYml().saveConfig();
        player.sendMessage(Translate.colorc("&bYour flying state has been restored."));
    }

    public void sendPermissionErrorMessage(CommandSender sender) {
        sender.sendMessage(Translate.color("&4You do not have permission to do this command."));
    }

    public String parseMessageString(Player player, String message) {
        return this.getMessagesUtils().parseMessageString(player, message);
    }

    public Component parseMessage(Player player, String message) {
        return this.getMessagesUtils().parseMessage(player, message);
    }

    public ConfigUtils getConfigUtils() {
        return configUtils;
    }

    public MessagesUtils getMessagesUtils() {
        return messagesUtils;
    }

    private String getDateOfBuildFromTxtFile() {
        if (this.dateOfBuild == null) {
            try (InputStream inputStream = this.plugin.getResource("date-of-build.txt")) {
                if (inputStream == null) {
                    this.dateOfBuild = "Unknown";
                    return this.dateOfBuild;
                }
                this.dateOfBuild = CharStreams.toString(new InputStreamReader(inputStream));

                // If first character is 0, remove it.
                if (this.dateOfBuild.charAt(0) == '0') {
                    this.dateOfBuild = this.dateOfBuild.substring(1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.dateOfBuild;
    }
}