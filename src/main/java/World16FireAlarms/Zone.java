package World16FireAlarms;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("Zone")
public class Zone implements ConfigurationSerializable {

    private String name;
    private Integer floor;

    public Zone(String name, Integer floor) {
        this.name = name;
        this.floor = floor;
    }

    public String getName() {
        return this.name;
    }

    public Integer getFloor() {
        return this.floor;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", this.name);
        map.put("Floor", this.floor);
        return map;
    }

    public static Zone deserialize(Map<String, Object> map) {
        return new Zone((String) map.get("Name"), (Integer) map.get("Floor"));
    }
}