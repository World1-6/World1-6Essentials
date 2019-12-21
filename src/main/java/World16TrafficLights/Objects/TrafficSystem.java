package World16TrafficLights.Objects;

import World16.Main.Main;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TrafficSystem implements ConfigurationSerializable {

    private Main plugin;
    private int currentTick;
    private int currentTrafficLightSystem;

    //SAVE
    private TrafficSystemType trafficSystemType;

    //SAVING SOLD SEPAERED
    private Map<Integer, TrafficLightSystem> trafficLightSystemMap;

    public TrafficSystem(Main plugin, TrafficSystemType trafficSystemType) {
        this.plugin = plugin;
        this.trafficSystemType = trafficSystemType;

        this.trafficLightSystemMap = new HashMap<>();
        this.currentTick = 0;
    }

    public void tick() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (currentTick <= 10) {
                    //ONLY RUN ONE TIME.
                    if (currentTick == 0) {
                        //GREEN
                        trafficLightSystemMap.get(currentTrafficLightSystem).doLight(TrafficLightState.GREEN);
                        trafficLightSystemMap.entrySet().stream().filter(key -> key.getKey() != currentTrafficLightSystem).forEach((k -> k.getValue().doLight(TrafficLightState.RED)));
                    }
                } else if (currentTick <= 14) {
                    //YELLOW
                    //ONLY RUN ONE TIME
                    if (currentTick == 11)
                        trafficLightSystemMap.get(currentTrafficLightSystem).doLight(TrafficLightState.YELLOW);
                } else if (currentTick == 15) {
                    //RED
                    trafficLightSystemMap.get(currentTrafficLightSystem).doLight(TrafficLightState.RED);

                    if (currentTrafficLightSystem == 0) currentTrafficLightSystem = 1;
                    else if (currentTrafficLightSystem == 1) currentTrafficLightSystem = 0;

                    currentTick = 0;
                    return;
                }
                currentTick++;
            }
        }.runTaskTimer(this.plugin, 20, 20);
    }

    public Main getPlugin() {
        return plugin;
    }

    public void setPlugin(Main plugin) {
        this.plugin = plugin;
    }

    public TrafficSystemType getTrafficSystemType() {
        return trafficSystemType;
    }

    public void setTrafficSystemType(TrafficSystemType trafficSystemType) {
        this.trafficSystemType = trafficSystemType;
    }

    public Map<Integer, TrafficLightSystem> getTrafficLightSystemMap() {
        return trafficLightSystemMap;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("trafficSystemType", trafficSystemType.toString());
        return map;
    }

    public static TrafficSystem deserialize(Map<String, Object> map) {
        return new TrafficSystem(Main.getPlugin(), TrafficSystemType.valueOf((String) map.get("trafficSystemType")));
    }
}
