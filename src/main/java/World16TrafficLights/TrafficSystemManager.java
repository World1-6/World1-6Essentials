package World16TrafficLights;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.CustomYmlManager;
import World16TrafficLights.Objects.TrafficLight;
import World16TrafficLights.Objects.TrafficLightSystem;
import World16TrafficLights.Objects.TrafficSystem;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

public class TrafficSystemManager {

    private Map<String, TrafficSystem> trafficSystemMap;

    private Main plugin;
    private CustomYmlManager trafficLightYML;

    private boolean isEnabled;

    public TrafficSystemManager(Main plugin, CustomConfigManager customConfigManager, boolean isEnabled) {
        this.plugin = plugin;
        this.trafficLightYML = customConfigManager.getTrafficLightYml();
        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();
        this.isEnabled = isEnabled;
    }

    public void loadAll() {
        if (!isEnabled) return;

        ConfigurationSection mCS = trafficLightYML.getConfig().getConfigurationSection("TrafficSystems");
        if (mCS == null) {
            trafficLightYML.getConfig().createSection("TrafficSystems");
            trafficLightYML.saveConfigSilent();
            return; //DON'T LOAD
        }

        for (String key : mCS.getKeys(false)) {
            ConfigurationSection trafficSystem = mCS.getConfigurationSection(key);

            TrafficSystem trafficSystemObject = (TrafficSystem) trafficSystem.get("TrafficSystem");

            ConfigurationSection trafficLightSystems = trafficSystem.getConfigurationSection("trafficLightSystems");
            for (String trafficLightSystemsKey : trafficLightSystems.getKeys(false)) {
                TrafficLightSystem trafficLightSystem = (TrafficLightSystem) trafficLightSystems.get(trafficLightSystemsKey);

                ConfigurationSection trafficLights = trafficLightSystems.getConfigurationSection("TrafficLights");
                for (String trafficLightsKey : trafficLights.getKeys(false)) {
                    trafficLightSystem.getTrafficLightMap().put(Integer.valueOf(trafficLightsKey), (TrafficLight) trafficLights.get(trafficLightsKey));
                }

                trafficSystemObject.getTrafficLightSystemMap().put(Integer.valueOf(trafficLightSystemsKey), trafficLightSystem);
            }
        }
    }

    public void saveAll() {
        if (!isEnabled) return;

        ConfigurationSection mCS = trafficLightYML.getConfig().getConfigurationSection("TrafficSystems");
        if (mCS == null) {
            mCS = trafficLightYML.getConfig().createSection("TrafficSystems");
            trafficLightYML.saveConfigSilent();
        }

        for (Map.Entry<String, TrafficSystem> entry : trafficSystemMap.entrySet()) {
            String k = entry.getKey().toLowerCase();
            TrafficSystem v = entry.getValue();

            ConfigurationSection trafficSystem = mCS.getConfigurationSection(k);
            if (trafficSystem == null) {
                trafficSystem = mCS.createSection(k);
                this.trafficLightYML.saveConfigSilent();
            }

            //trafficLightSystems
            ConfigurationSection trafficLightSystems = trafficSystem.getConfigurationSection("trafficLightSystems");
            if (trafficLightSystems == null) {
                trafficLightSystems = trafficSystem.createSection("trafficLightSystems");
                trafficLightYML.saveConfigSilent();
            }
            for (Map.Entry<Integer, TrafficLightSystem> e : v.getTrafficLightSystemMap().entrySet()) {
                Integer key = e.getKey();
                TrafficLightSystem value = e.getValue();

                ConfigurationSection trafficLights = trafficLightSystems.getConfigurationSection("TrafficLights");
                if (trafficLights == null) {
                    trafficLights = trafficLightSystems.createSection("TrafficLights");
                    trafficLightYML.saveConfigSilent();
                }
                for (Map.Entry<Integer, TrafficLight> mapEntry : value.getTrafficLightMap().entrySet()) {
                    Integer integer = mapEntry.getKey();
                    TrafficLight trafficLight = mapEntry.getValue();
                    trafficLights.set(String.valueOf(integer), trafficLight);
                }

                trafficLightSystems.set(String.valueOf(key), value);
            }

            trafficSystem.set("TrafficSystem", v);
            trafficLightYML.saveConfigSilent();
        }
    }
}
