package com.andrew121410.World16.Managers;

import com.andrew121410.World16.Main.Main;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class JailManager {

    //Maps
    private Map<String, Location> jailsMap;
    //...

    private Main plugin;

    private CustomYmlManager jailsYml;

    public JailManager(CustomConfigManager customConfigManager, Main plugin) {
        this.plugin = plugin;
        this.jailsYml = customConfigManager.getJailsYml();

        this.jailsMap = this.plugin.getSetListMap().getJails();
    }

    public void loadAllJails() {
        ConfigurationSection cs = this.jailsYml.getConfig().getConfigurationSection("Jails");
        if (cs == null) {
            this.jailsYml.getConfig().createSection("Jails");
            this.jailsYml.saveConfig();
            return;
        }

        ConfigurationSection jailConfig = null;

        for (String jailName : cs.getKeys(false)) {
            jailConfig = cs.getConfigurationSection(jailName);

            Location location = (Location) jailConfig.get("Location");

            jailsMap.put(jailName, location);
        }
    }

    public void saveAllJails() {
        ConfigurationSection cs = this.jailsYml.getConfig().getConfigurationSection("Jails");
        if (cs == null) {
            cs = this.jailsYml.getConfig().createSection("Jails");
            this.jailsYml.saveConfig();
        }

        for (Map.Entry<String, Location> entry : this.jailsMap.entrySet()) {
            String k = entry.getKey();
            Location v = entry.getValue();

            ConfigurationSection jailConfig = cs.getConfigurationSection(k.toLowerCase());
            if (jailConfig == null) {
                jailConfig = cs.createSection(k.toLowerCase());
                this.jailsYml.saveConfig();
            }

            jailConfig.set("Location", v);
            this.jailsYml.saveConfig();
        }
    }

    public boolean delete(String jailName) {
        if (jailsMap.get(jailName.toLowerCase()) == null) {
            return false;
        }
        jailsMap.remove(jailName.toLowerCase());

        ConfigurationSection jails = this.jailsYml.getConfig().getConfigurationSection("Jails");
        jails.set(jailName.toLowerCase(), null);
        this.jailsYml.saveConfig();
        return true;
    }
}
