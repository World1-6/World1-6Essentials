package World16.Managers;

import World16.Main.Main;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class WarpManager {

    //Maps
    private Map<String, Location> warpsMap;
    //...

    private Main plugin;

    private CustomYmlManager warpsYml;

    public WarpManager(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.warpsYml = customConfigManager.getWarpsYml();

        this.warpsMap = this.plugin.getSetListMap().getWarpsMap();
    }

    public void loadAllWarps() {
        ConfigurationSection cs = this.warpsYml.getConfig().getConfigurationSection("Warps");
        if (cs == null) {
            cs = this.warpsYml.getConfig().createSection("Warps");
            this.warpsYml.saveConfigSilent();
        }

        ConfigurationSection warpsConfig = null;

        for (String key : cs.getKeys(false)) {
            warpsConfig = cs.getConfigurationSection(key);

            Location location = (Location) warpsConfig.get("Location");
            this.warpsMap.putIfAbsent(key, location);
        }
    }

    public void saveAllWarps() {
        ConfigurationSection cs = this.warpsYml.getConfig().getConfigurationSection("Warps");
        if (cs == null) {
            cs = this.warpsYml.getConfig().createSection("Warps");
            this.warpsYml.saveConfigSilent();
        }

        for (Map.Entry<String, Location> entry : this.warpsMap.entrySet()) {
            String k = entry.getKey();
            Location v = entry.getValue();

            ConfigurationSection warpsConfig = cs.getConfigurationSection(k.toLowerCase());
            if (warpsConfig == null) {
                warpsConfig = cs.createSection(k.toLowerCase());
                this.warpsYml.saveConfigSilent();
            }

            warpsConfig.set("Location", v);
            this.warpsYml.saveConfigSilent();
        }
    }

    public void deleteWarp(String key) {
        if (this.warpsMap.get(key.toLowerCase()) == null) {
            return;
        }

        this.warpsMap.remove(key);

        ConfigurationSection cs = this.warpsYml.getConfig().getConfigurationSection("Warps");
        cs.set(key.toLowerCase(), null);
        this.warpsYml.saveConfigSilent();
    }
}
