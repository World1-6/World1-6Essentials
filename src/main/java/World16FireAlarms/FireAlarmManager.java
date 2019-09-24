package World16FireAlarms;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.CustomYmlManager;
import World16.Utils.Translate;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class FireAlarmManager {

    private Main plugin;

    private CustomYmlManager fireAlarmsYml;

    private Map<String, IFireAlarm> fireAlarmMap;

    public FireAlarmManager(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.fireAlarmsYml = customConfigManager.getFireAlarmsYml();

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();
    }

    public void loadFireAlarms() {

        //Only runs when firealarms.yml is first being created.
        ConfigurationSection cs = this.fireAlarmsYml.getConfig().getConfigurationSection("FireAlarms");
        if (cs == null) {
            this.fireAlarmsYml.getConfig().createSection("FireAlarms");
            this.fireAlarmsYml.saveConfigSilent();
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat("&c[FireAlarmManager]&r&6 FireAlarm section has been created."));
            return;
        }

        for (String fireAlarm : cs.getKeys(false)) {
            ConfigurationSection fireAlarmConfig = cs.getConfigurationSection(fireAlarm);
            IFireAlarm iFireAlarm = (IFireAlarm) fireAlarmConfig.get("IFireAlarm");

            ConfigurationSection strobesConfig = fireAlarmConfig.getConfigurationSection("Strobes");
            for (String strobes : strobesConfig.getKeys(false)) {
                iFireAlarm.registerStrobe((IStrobe) strobesConfig.get(strobes));
            }

            ConfigurationSection zonesConfig = fireAlarmConfig.getConfigurationSection("Zones");
            for (String zone : zonesConfig.getKeys(false)) {
                iFireAlarm.registerZone((Zone) zonesConfig.get(zone));
            }
            fireAlarmMap.putIfAbsent(fireAlarm, iFireAlarm);
        }
    }

    public void saveFireAlarms() {
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

            this.fireAlarmsYml.saveConfigSilent();
        }
        this.fireAlarmsYml.saveConfigSilent();
    }
}
