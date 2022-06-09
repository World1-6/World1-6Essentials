package com.andrew121410.mc.world16essentials.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DataTranslator {

    public DataTranslator() {
    }

    public Map<UUID, Map<String, Location>> convertHomesFrom(Software software) {
        Map<UUID, Map<String, Location>> globalHomes = new HashMap<>();
        switch (software) {
            case ESSENTIALS_X -> {
                if (hasEssentialsConfigFolder() == null) {
                    throw new NullPointerException("World1-6Ess DataTranslator: convertHomesFrom -> ESSENTIALS_X -> hasEssentialsConfigFolder was null");
                }
                List<File> userDataFiles = getAllUserDataYmlFiles();

                for (File userDataFile : userDataFiles) {
                    String stringUUID = userDataFile.getName().replace(".yml", "");
                    UUID uuid = UUID.fromString(stringUUID);

                    FileConfiguration config = loadConfig(userDataFile);
                    ConfigurationSection homesSelection = config.getConfigurationSection("homes");
                    if (homesSelection == null) {
                        // User has no homes
                        continue;
                    }
                    Map<String, Location> homesMap = new HashMap<>();
                    for (String homeName : homesSelection.getKeys(false)) {
                        ConfigurationSection homeSelection = homesSelection.getConfigurationSection(homeName);
                        UUID worldUUID = UUID.fromString(homeSelection.getString("world"));
                        double x = homeSelection.getDouble("x");
                        double y = homeSelection.getDouble("y");
                        double z = homeSelection.getDouble("z");
                        float yaw = (float) homeSelection.getDouble("yaw");
                        float pitch = (float) homeSelection.getDouble("pitch");
                        Location location = new Location(Bukkit.getWorld(worldUUID), x, y, z, yaw, pitch);
                        homesMap.put(homeName, location);
                    }
                    globalHomes.put(uuid, homesMap);
                }
                return globalHomes;
            }
        }
        return null;
    }

    public void convertHomesTo(Software software, Map<UUID, Map<String, Location>> map) {
        switch (software) {
        }
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

    public FileConfiguration loadConfig(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}