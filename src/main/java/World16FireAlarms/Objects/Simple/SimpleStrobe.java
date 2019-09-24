package World16FireAlarms.Objects.Simple;

import World16FireAlarms.interfaces.IStrobe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("IStrobe")
public class SimpleStrobe implements IStrobe, ConfigurationSerializable {

    private String name;
    private Location location;
    private String zone;

    public SimpleStrobe(Location block, String name, String zoneName) {
        this.location = block;
        this.zone = zoneName;
        this.name = name;
    }

    public SimpleStrobe(Block block, String name, String zoneName) {
        this(block.getLocation(), name, zoneName);
    }

    public void on() {
        this.location.getBlock().setType(Material.REDSTONE_LAMP_ON);
    }

    public void off() {
        this.location.getBlock().setType(Material.REDSTONE_LAMP_OFF);
    }

    public void sound() {
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Location", this.location);
        map.put("Name", this.name);
        map.put("Zone", this.zone);
        return map;
    }

    public static SimpleStrobe deserialize(Map<String, Object> map) {
        return new SimpleStrobe((Location) map.get("Location"), (String) map.get("Name"), (String) map.get("Zone"));
    }
}