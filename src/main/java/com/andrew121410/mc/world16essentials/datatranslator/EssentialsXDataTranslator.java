package com.andrew121410.mc.world16essentials.datatranslator;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.config.EssentialsUserConfiguration;
import com.earth2me.essentials.config.entities.LazyLocation;
import com.earth2me.essentials.config.holders.UserConfigHolder;
import com.earth2me.essentials.libs.configurate.serialize.SerializationException;
import com.earth2me.essentials.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class EssentialsXDataTranslator implements IDataTranslator {

    private final World16Essentials plugin;

    public EssentialsXDataTranslator(World16Essentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean convertFrom() {
        this.homesFrom();
        this.warpsFrom();
        return true;
    }

    @Override
    public boolean convertTo() {
        this.homesTo();
        this.warpsTo();
        return true;
    }

    private void homesFrom() {
        if (hasEssentialsConfigFolder() == null) return;

        List<File> userDataFiles = getAllUserDataYmlFiles();
        for (File userDataFile : userDataFiles) {
            String stringUUID = userDataFile.getName().replace(".yml", "");
            UUID uuid = UUID.fromString(stringUUID);

            FileConfiguration config = DataTranslator.loadConfig(userDataFile);
            ConfigurationSection homesSelection = config.getConfigurationSection("homes");
            if (homesSelection == null) {
                // User has no homes
                continue;
            }
            for (String homeName : homesSelection.getKeys(false)) {
                ConfigurationSection homeSelection = homesSelection.getConfigurationSection(homeName);
                UUID worldUUID = UUID.fromString(homeSelection.getString("world"));
                double x = homeSelection.getDouble("x");
                double y = homeSelection.getDouble("y");
                double z = homeSelection.getDouble("z");
                float yaw = (float) homeSelection.getDouble("yaw");
                float pitch = (float) homeSelection.getDouble("pitch");
                Location location = new Location(Bukkit.getWorld(worldUUID), x, y, z, yaw, pitch);

                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    this.plugin.getHomeManager().add(player, homeName, location);
                } else {
                    this.plugin.getHomeManager().save(uuid, "null", homeName, location);
                }
            }
        }
    }

    private void homesTo() {
        Essentials essentials = getEssentials();
        if (essentials == null) return;

        // Load all homes including offline players
        Map<UUID, Map<String, Location>> allHomes = this.plugin.getHomeManager().loadAllHomesForAllPlayersIncludingOfflinePlayers();

        for (Map.Entry<UUID, Map<String, Location>> uuidMapEntry : allHomes.entrySet()) {
            UUID uuid = uuidMapEntry.getKey();
            Map<String, Location> homes = uuidMapEntry.getValue();
            Player player = Bukkit.getPlayer(uuid);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            if (player != null && player.isOnline()) {
                User user = essentials.getUser(player);
                homes.forEach(user::setHome);
            } else {
                MyEssentialsUser myEssentialsUser = new MyEssentialsUser(essentials, offlinePlayer);
                homes.forEach(myEssentialsUser::setHome);
            }
        }
    }

    private void warpsFrom() {
        Essentials essentials = getEssentials();

        for (String warpName : essentials.getWarps().getList()) {
            Location location = null;
            try {
                location = essentials.getWarps().getWarp(warpName);
            } catch (Exception ignored) {

            }

            if (location != null) {
                this.plugin.getWarpManager().createWarp(warpName, location);
            }
        }
    }

    private void warpsTo() {
        Essentials essentials = getEssentials();

        this.plugin.getSetListMap().getWarpsMap().forEach((warpName, location) -> {
            try {
                essentials.getWarps().setWarp(warpName, location);
            } catch (Exception ignored) {
            }
        });
    }

    public Essentials getEssentials() {
        return (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
    }

    private File hasEssentialsConfigFolder() {
        File file = new File("plugins/Essentials/");
        return file.exists() ? file : null;
    }

    private List<File> getAllUserDataYmlFiles() {
        if (hasEssentialsConfigFolder() == null) return null;
        File userDataFolder = new File(hasEssentialsConfigFolder(), "userdata/");
        return Arrays.stream(Objects.requireNonNull(userDataFolder.listFiles())).filter(file -> file.getName().endsWith(".yml")).collect(Collectors.toList());
    }
}

class MyEssentialsUser {

    private final IEssentials iEssentials;
    private final EssentialsUserConfiguration config;
    private UserConfigHolder holder;

    public MyEssentialsUser(IEssentials iEssentials, OfflinePlayer offlinePlayer) {
        this.iEssentials = iEssentials;

        File folder = new File(iEssentials.getDataFolder(), "userdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filename;
        try {
            filename = offlinePlayer.getUniqueId().toString();
        } catch (Throwable var6) {
            iEssentials.getLogger().warning("Falling back to old username system for " + offlinePlayer.getName());
            filename = offlinePlayer.getName();
        }

        this.config = new EssentialsUserConfiguration(offlinePlayer.getName(), offlinePlayer.getUniqueId(), new File(folder, filename + ".yml"));
        this.reloadConfig();
        if (this.config.getUsername() == null) {
            this.config.setUsername(offlinePlayer.getName());
        }
    }

    public void reloadConfig() {
        this.config.load();

        try {
            this.holder = (UserConfigHolder) this.config.getRootNode().get(UserConfigHolder.class);
        } catch (SerializationException var2) {
            this.iEssentials.getLogger().log(Level.SEVERE, "Error while reading user config: " + var2.getMessage(), var2);
            throw new RuntimeException(var2);
        }

        this.config.setSaveHook(() -> {
            try {
                this.config.getRootNode().set(UserConfigHolder.class, this.holder);
            } catch (SerializationException var2) {
                this.iEssentials.getLogger().log(Level.SEVERE, "Error while saving user config: " + var2.getMessage(), var2);
                throw new RuntimeException(var2);
            }
        });
    }

    public void setHome(String name, final Location loc) {
        name = StringUtil.safeString(name);
        this.holder.homes().put(name, LazyLocation.fromLocation(loc));
        this.config.save();
    }
}
