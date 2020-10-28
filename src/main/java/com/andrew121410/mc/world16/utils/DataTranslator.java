package com.andrew121410.mc.world16.utils;

import com.andrew121410.ccutils.storage.SQLite;
import com.andrew121410.ccutils.storage.easy.EasySQL;
import com.andrew121410.ccutils.storage.easy.SQLDataStore;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Right now only homes.
 */

public class DataTranslator {

    public DataTranslator() {
    }

    public Map<UUID, Map<String, Location>> convertHomesFrom(Software software) {
        Map<UUID, Map<String, Location>> globalHomes = new HashMap<>();
        switch (software) {
            case ESSENTIALS_X:
                if (hasEssentialsConfigFolder() == null)
                    throw new NullPointerException("DataTranslator : loadHomes -> ESSENTIALS_X hasEssentialsConfigFolder was null");
                List<File> userDataFiles = getAllHomesFromEssentialsX();
                for (File userDataFile : userDataFiles) {
                    UUID uuid = UUID.fromString(userDataFile.getName().substring(userDataFile.getName().length() - 3));
                    FileConfiguration config = loadConfig(userDataFile);
                    ConfigurationSection homesSelection = config.getConfigurationSection("homes");
                    if (homesSelection == null)
                        throw new NullPointerException("DataTranslator : loadHomes -> ESSENTIALS_X homesSelection was null");
                    Map<String, Location> homesMap = new HashMap<>();
                    for (String homeName : homesSelection.getKeys(false)) {
                        ConfigurationSection homeSelection = homesSelection.getConfigurationSection(homeName);
                        String worldName = homeSelection.getString("world");
                        int x = (int) homeSelection.get("x");
                        int y = (int) homeSelection.get("y");
                        int z = (int) homeSelection.get("z");
                        float yaw = (float) homeSelection.get("yaw");
                        float pitch = (float) homeSelection.get("pitch");
                        Location location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                        homesMap.put(homeName, location);
                    }
                    globalHomes.put(uuid, homesMap);
                }
                return globalHomes;
            case ANDREWS_ESSENTIALS_FABRIC_MOD:
                SQLite sqLite = new SQLite(new File("Andrews-Config"), "Homes");
                EasySQL easySQL = new EasySQL(sqLite, "Homes");
                Multimap<String, SQLDataStore> bigHomesMap = null;
                try {
                    bigHomesMap = easySQL.getEverything();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (bigHomesMap == null) return null;
                bigHomesMap.asMap().forEach((k, v) -> {
                    UUID uuid = UUID.fromString(k);
                    Map<String, Location> homesMap = new HashMap<>();
                    v.forEach(sqlDataStore -> {
                        String homeName = sqlDataStore.get("HomeName");
                        int world = Integer.parseInt(sqlDataStore.get("World"));
                        double x = Double.parseDouble(sqlDataStore.get("X"));
                        double y = Double.parseDouble(sqlDataStore.get("Y"));
                        double z = Double.parseDouble(sqlDataStore.get("Z"));
                        float yaw = Float.parseFloat(sqlDataStore.get("YAW"));
                        float pitch = Float.parseFloat(sqlDataStore.get("PITCH"));
                        Location location = new Location(Bukkit.getServer().getWorld(convertFromOldIntToBukkitStringWorld(world)), (int) x, (int) y, (int) z, yaw, pitch);
                        homesMap.put(homeName, location);
                    });
                    globalHomes.put(uuid, homesMap);
                });
                return globalHomes;
        }
        return null;
    }

    public void convertHomesTo(Software software, Map<UUID, Map<String, Location>> map) {
        switch (software) {
            case ANDREWS_ESSENTIALS_FABRIC_MOD:
                SQLite sqLite = new SQLite(new File("Andrews-Config"), "Homes");
                EasySQL easySQL = new EasySQL(sqLite, "Homes");
                map.forEach((uuid, theMap) -> {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    theMap.forEach((homeName, location) -> {
                        SQLDataStore sqlDataStore = new SQLDataStore();
                        sqlDataStore.put("UUID", uuid.toString());
                        sqlDataStore.put("Date", "0");
                        sqlDataStore.put("PlayerName", offlinePlayer.getName());
                        sqlDataStore.put("HomeName", homeName);
                        sqlDataStore.put("X", String.valueOf(location.getBlockX()));
                        sqlDataStore.put("Y", String.valueOf(location.getBlockY()));
                        sqlDataStore.put("Z", String.valueOf(location.getBlockZ()));
                        sqlDataStore.put("YAW", String.valueOf(location.getYaw()));
                        sqlDataStore.put("PITCH", String.valueOf(location.getPitch()));
                        sqlDataStore.put("World", String.valueOf(convertToOldIntToBukkitStringWorld(location.getWorld().getName())));
                        try {
                            easySQL.save(sqlDataStore);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    });
                });
        }

    }

    private String convertFromOldIntToBukkitStringWorld(int var) {
        switch (var) {
            case -1:
                return "world_nether";
            case 0:
                return "world";
            case 1:
                return "world_the_end";
        }
        return "fuck";
    }

    private int convertToOldIntToBukkitStringWorld(String world) {
        switch (world) {
            case "world_nether":
                return -1;
            case "world":
                return 0;
            case "world_the_end":
                return 1;
        }
        return 3434;
    }

    private File hasEssentialsConfigFolder() {
        File file = new File("plugins/Essentials/");
        return file.exists() ? file : null;
    }

    private List<File> getAllHomesFromEssentialsX() {
        if (hasEssentialsConfigFolder() == null) return null;
        File userDataFolder = new File(hasEssentialsConfigFolder(), "userdata/");
        return Arrays.stream(Objects.requireNonNull(userDataFolder.listFiles())).filter(file -> file.getName().endsWith(".yml")).collect(Collectors.toList());
    }

    public FileConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}