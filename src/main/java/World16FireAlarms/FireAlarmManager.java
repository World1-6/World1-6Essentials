package World16FireAlarms;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.CustomYmlManager;
import World16.Utils.Translate;
import World16FireAlarms.Objects.Zone;
import World16FireAlarms.Screen.FireAlarmScreen;
import World16FireAlarms.interfaces.IFireAlarm;
import World16FireAlarms.interfaces.IStrobe;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class FireAlarmManager {

    private Main plugin;

    private boolean on = false;

    private CustomYmlManager fireAlarmsYml;

    private Map<String, IFireAlarm> fireAlarmMap;
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;

    public FireAlarmManager(Main plugin, CustomConfigManager customConfigManager, boolean on) {
        if (!on) return;
        this.on = true;

        this.plugin = plugin;

        this.fireAlarmsYml = customConfigManager.getFireAlarmsYml();

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();
        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();
    }

    public void loadFireAlarms() {
        if (!on) return;

        //Only runs when firealarms.yml is first being created.
        ConfigurationSection cs = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms");
        ConfigurationSection cs1 = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarmScreens");
        if (cs == null || cs1 == null) {
            if (cs == null) {
                this.fireAlarmsYml.getConfig().createSection("FireAlarms");
                this.fireAlarmsYml.saveConfigSilent();
                this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat("&c[FireAlarmManager]&r&6 FireAlarm section has been created."));
            }
            if (cs1 == null) {
                this.fireAlarmsYml.getConfig().createSection("FireAlarmScreens");
                this.fireAlarmsYml.saveConfigSilent();
            }
            return;
        }

        //For each fire alarm.
        for (String fireAlarm : cs.getKeys(false)) {
            cs = cs.getConfigurationSection(fireAlarm);
            IFireAlarm iFireAlarm = (IFireAlarm) cs.get("IFireAlarm");

            //Strobes
            ConfigurationSection strobesConfig = cs.getConfigurationSection("Strobes");
            if (strobesConfig != null) {
                for (String strobes : strobesConfig.getKeys(false)) {
                    iFireAlarm.registerStrobe((IStrobe) strobesConfig.get(strobes));
                }
            }

            //Zones
            ConfigurationSection zonesConfig = cs.getConfigurationSection("Zones");
            if (zonesConfig != null) {
                for (String zone : zonesConfig.getKeys(false)) {
                    iFireAlarm.registerZone((Zone) zonesConfig.get(zone));
                }
            }

            //Signs
            ConfigurationSection signsConfig = cs.getConfigurationSection("Signs");
            if (signsConfig != null) {
                for (String sign : signsConfig.getKeys(false)) {
                    iFireAlarm.registerSign(sign, (Location) signsConfig.get(sign));
                }
            }
            fireAlarmMap.putIfAbsent(fireAlarm, iFireAlarm);
        }

        //For each fire alarm screen
        for (String fireAlarmScreenName : cs1.getKeys(false)) {
            ConfigurationSection cs2 = cs1.getConfigurationSection(fireAlarmScreenName);
            FireAlarmScreen fireAlarmScreen = (FireAlarmScreen) cs2.get("FireAlarmScreen");
            this.fireAlarmScreenMap.putIfAbsent(fireAlarmScreen.getLocation(), fireAlarmScreen);
        }
    }

    public void saveFireAlarms() {
        if (!on) return;

        //For each fire alarm
        for (Map.Entry<String, IFireAlarm> entry : fireAlarmMap.entrySet()) {
            String k = entry.getKey();
            IFireAlarm v = entry.getValue();

            String fireAlarmLocation = "FireAlarms" + "." + k.toLowerCase();

            ConfigurationSection fireAlarmConfig = this.fireAlarmsYml.getConfig().getConfigurationSection(fireAlarmLocation);
            if (fireAlarmConfig == null) {
                fireAlarmConfig = this.fireAlarmsYml.getConfig().createSection(fireAlarmLocation);
                this.fireAlarmsYml.saveConfigSilent();
            }

            fireAlarmConfig.set("IFireAlarm", v);

            //Strobes
            ConfigurationSection fireAlarmStrobesConfig = fireAlarmConfig.getConfigurationSection("Strobes");
            if (fireAlarmStrobesConfig == null) {
                fireAlarmStrobesConfig = fireAlarmConfig.createSection("Strobes");
                this.fireAlarmsYml.saveConfigSilent();
            }

            for (Map.Entry<String, IStrobe> e : v.getStrobesMap().entrySet()) {
                String k1 = e.getKey();
                IStrobe v1 = e.getValue();
                fireAlarmStrobesConfig.set(k1, v1);
            }

            //Zones
            ConfigurationSection fireAlarmZoneConfig = fireAlarmConfig.getConfigurationSection("Zones");
            if (fireAlarmZoneConfig == null) {
                fireAlarmZoneConfig = fireAlarmConfig.createSection("Zones");
                this.fireAlarmsYml.saveConfigSilent();
            }

            for (Zone zone : v.getZones()) {
                fireAlarmZoneConfig.set(zone.getName(), zone);
            }

            //Signs
            ConfigurationSection fireAlarmSignConfig = fireAlarmConfig.getConfigurationSection("Signs");
            if (fireAlarmSignConfig == null) {
                fireAlarmSignConfig = fireAlarmConfig.createSection("Signs");
                this.fireAlarmsYml.saveConfigSilent();
            }

            for (Map.Entry<String, Location> e : v.getSigns().entrySet()) {
                String key = e.getKey();
                Location value = e.getValue();
                fireAlarmSignConfig.set(key.toLowerCase(), value);
            }

            this.fireAlarmsYml.saveConfigSilent();
        }

        //For each sign screen
        for (Map.Entry<Location, FireAlarmScreen> entry : fireAlarmScreenMap.entrySet()) {
            Location k = entry.getKey();
            FireAlarmScreen v = entry.getValue();

            String fireAlarmScreen = "FireAlarmScreens" + "." + v.getName().toLowerCase();

            ConfigurationSection fireAlarmScreenConfig = this.fireAlarmsYml.getConfig().getConfigurationSection(fireAlarmScreen);
            if (fireAlarmScreenConfig == null) {
                fireAlarmScreenConfig = this.fireAlarmsYml.getConfig().createSection(fireAlarmScreen);
                this.fireAlarmsYml.saveConfigSilent();
            }

            fireAlarmScreenConfig.set("FireAlarmScreen", v);
            this.fireAlarmsYml.saveConfigSilent();
        }
    }

    public void deleteFireAlarm(String name) {
        if (!on) return;

        if (this.fireAlarmMap.get(name) != null) {
            fireAlarmMap.remove(name);
            ConfigurationSection firealarmConfig = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms");
            firealarmConfig.set(name.toLowerCase(), null);
            this.fireAlarmsYml.saveConfigSilent();
        }
    }

    public void deleteFireAlarmStrobe(String fireAlarm, String strobeName) {
        if (!on) return;

        if (this.fireAlarmMap.get(fireAlarm) != null) {
            this.fireAlarmMap.get(fireAlarm).getStrobesMap().remove(strobeName);
            ConfigurationSection fireAlarmConfig = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms." + fireAlarm.toLowerCase());
            ConfigurationSection fireAlarmStrobes = fireAlarmConfig.getConfigurationSection("Strobes");
            fireAlarmStrobes.set(strobeName.toLowerCase(), null);
            this.fireAlarmsYml.saveConfigSilent();
        }
    }

    public void deleteFireAlarmSignScreen(Location location) {
        if (!on) return;

        if (this.fireAlarmScreenMap.get(location) != null) {
            String signName = this.fireAlarmScreenMap.get(location).getName();
            fireAlarmScreenMap.remove(location);
            ConfigurationSection firealarmConfig = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarmScreens");
            firealarmConfig.set(signName.toLowerCase(), null);
            this.fireAlarmsYml.saveConfigSilent();
        }
    }

    public void deleteFireAlarmSignScreen(String fireAlarmName, String signName, Location location) {
        if (!on) return;

        if (this.fireAlarmMap.get(fireAlarmName.toLowerCase()) != null) {
            fireAlarmScreenMap.remove(location);
            fireAlarmMap.get(fireAlarmName).getSigns().remove(signName);

            ConfigurationSection cs = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms." + fireAlarmName.toLowerCase());
            ConfigurationSection cs1 = cs.getConfigurationSection("Signs");

            cs1.set(signName.toLowerCase(), null);

            ConfigurationSection firealarmConfig = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarmScreens");
            firealarmConfig.set(signName.toLowerCase(), null);
            this.fireAlarmsYml.saveConfigSilent();
        }
    }
}
