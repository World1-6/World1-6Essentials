package World16TrafficLights.Objects;

import World16.Main.Main;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TrafficLightSystem implements ConfigurationSerializable {

    private Main plugin;

    private Map<Integer, TrafficLight> trafficLightMap;
    private boolean isTurningJunction;
    private TrafficLightState lightState;

    public TrafficLightSystem(Main plugin, boolean isTurningJunction) {
        this.plugin = plugin;
        this.isTurningJunction = isTurningJunction;
        this.trafficLightMap = new HashMap<>();
    }

    public TrafficLightSystem(Main plugin) {
        this(plugin, false);
    }

    public void doLight(TrafficLightState trafficLightState) {
        this.lightState = trafficLightState;

        Iterator<Map.Entry<Integer, TrafficLight>> iterator = trafficLightMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, TrafficLight> entry = iterator.next();
            if (entry.getValue().doLight(trafficLightState)) {
                //SUCCUS
            } else {
                //FAILELD
                iterator.remove();

            }
        }
    }

    public Map<Integer, TrafficLight> getTrafficLightMap() {
        return trafficLightMap;
    }

    public Main getPlugin() {
        return plugin;
    }

    public TrafficLightState getLightState() {
        return lightState;
    }

    public boolean isTurningJunction() {
        return isTurningJunction;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("isTurningJunction", isTurningJunction);
        return map;
    }

    public static TrafficLightSystem deserialize(Map<String, Object> map) {
        return new TrafficLightSystem(Main.getPlugin(), (boolean) map.get("isTurningJunction"));
    }
}
