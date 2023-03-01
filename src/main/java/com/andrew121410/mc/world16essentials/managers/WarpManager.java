package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class WarpManager {

    private final Map<String, Location> warpsMap;

    private final World16Essentials plugin;
    private final CustomYmlManager warpsYml;

    public WarpManager(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.warpsYml = customConfigManager.getWarpsYml();
        this.warpsMap = this.plugin.getSetListMap().getWarpsMap();
    }

    public void loadAllWarps() {
        ConfigurationSection cs = getConfigurationSection();
        for (String key : cs.getKeys(false)) {
            ConfigurationSection warpCs = cs.getConfigurationSection(key);

            Object object = warpCs.get("Location");
            if (object instanceof Location location) { // Need to convert it to UnlinkedWorldLocation
                warpCs.set("Location", new UnlinkedWorldLocation(location));
                this.warpsYml.saveConfig();
            }

            UnlinkedWorldLocation location = (UnlinkedWorldLocation) warpCs.get("Location");
            this.warpsMap.putIfAbsent(key, location.toLocation());
        }
    }

    public void add(String name, Location location) {
        String newWarpName = name.toLowerCase();

        this.warpsMap.put(newWarpName, location);
        ConfigurationSection cs = getConfigurationSection();
        ConfigurationSection warpCs = cs.createSection(newWarpName);
        warpCs.set("Location", new UnlinkedWorldLocation(location));
        this.warpsYml.saveConfig();
    }

    public void delete(String name) {
        String newWarpName = name.toLowerCase();
        if (!this.warpsMap.containsKey(newWarpName)) return;
        ConfigurationSection cs = getConfigurationSection();
        cs.set(newWarpName, null);
        this.warpsYml.saveConfig();
        this.warpsMap.remove(newWarpName);
    }

    private ConfigurationSection getConfigurationSection() {
        ConfigurationSection cs = this.warpsYml.getConfig().getConfigurationSection("Warps");
        if (cs == null) {
            this.warpsYml.getConfig().createSection("Warps");
            this.warpsYml.saveConfig();
            this.warpsYml.reloadConfig();
            // Get it again
            cs = this.warpsYml.getConfig().getConfigurationSection("Warps");
        }
        return cs;
    }
}