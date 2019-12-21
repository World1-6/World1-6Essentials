package World16TrafficLights.Objects;

import World16.Main.Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TrafficLightSystem {

    private Main plugin;

    private Map<Integer, TrafficLight> trafficLightMap;
    private TrafficLightState currentTrafficLightState;

    public TrafficLightSystem(Main plugin) {
        this.plugin = plugin;
        this.trafficLightMap = new HashMap<>();
    }

    public void doLight(TrafficLightState trafficLightState) {
        this.currentTrafficLightState = trafficLightState;

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

    public TrafficLightState getCurrentTrafficLightState() {
        return currentTrafficLightState;
    }
}