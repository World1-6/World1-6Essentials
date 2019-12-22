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
            ConfigurationSection trafficLightSystems = trafficSystem.getConfigurationSection("TrafficLightSystems");

            TrafficSystem trafficSystemObject = (TrafficSystem) trafficSystem.get("TrafficSystem");

            for (String trafficLightSystemsKey : trafficLightSystems.getKeys(false)) {
                ConfigurationSection trafficLightSystem = trafficLightSystems.getConfigurationSection(trafficLightSystemsKey);
                ConfigurationSection lights = trafficLightSystem.getConfigurationSection("Lights");

                TrafficLightSystem trafficLightSystemObject = (TrafficLightSystem) trafficLightSystem.get("TrafficLightSystem");

                for (String lightsKey : lights.getKeys(false)) {
                    TrafficLight trafficLight = (TrafficLight) lights.get(lightsKey);
                    trafficLightSystemObject.getTrafficLightMap().put(Integer.valueOf(lightsKey), trafficLight);
                }
                trafficSystemObject.getTrafficLightSystemMap().put(Integer.valueOf(trafficLightSystemsKey), trafficLightSystemObject);
            }
            trafficSystemObject.tick();
            this.trafficSystemMap.put(key, trafficSystemObject);
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

            trafficSystem.set("TrafficSystem", v);

            ConfigurationSection trafficLightSystems = trafficSystem.getConfigurationSection("TrafficLightSystems");
            if (trafficLightSystems == null) {
                trafficLightSystems = trafficSystem.createSection("TrafficLightSystems");
                this.trafficLightYML.saveConfigSilent();
            }
            for (Map.Entry<Integer, TrafficLightSystem> e : v.getTrafficLightSystemMap().entrySet()) {
                Integer key = e.getKey();
                TrafficLightSystem value = e.getValue();

                ConfigurationSection trafficLightSystem = trafficLightSystems.getConfigurationSection(String.valueOf(key));
                if (trafficLightSystem == null) {
                    trafficLightSystem = trafficLightSystems.createSection(String.valueOf(key));
                    this.trafficLightYML.saveConfigSilent();
                }
                trafficLightSystem.set("TrafficLightSystem", value);

                ConfigurationSection lights = trafficLightSystem.getConfigurationSection("Lights");
                if (lights == null) {
                    lights = trafficLightSystem.createSection("Lights");
                    this.trafficLightYML.saveConfigSilent();
                }
                for (Map.Entry<Integer, TrafficLight> mapEntry : value.getTrafficLightMap().entrySet()) {
                    Integer integer = mapEntry.getKey();
                    TrafficLight trafficLight = mapEntry.getValue();
                    lights.set(String.valueOf(integer), trafficLight);
                }
            }
            this.trafficLightYML.saveConfigSilent();
        }
    }
}