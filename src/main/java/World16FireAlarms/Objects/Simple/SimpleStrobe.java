package World16FireAlarms.Objects.Simple;

import World16FireAlarms.Objects.FireAlarmSound;
import World16FireAlarms.interfaces.IStrobe;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("IStrobe")
public class SimpleStrobe implements IStrobe, ConfigurationSerializable {

    private String name;
    private Location location;
    private String zone;

    private boolean isLight;

    private FireAlarmSound fireAlarmSound;

    public SimpleStrobe(Location block, String name, String zoneName) {
        this.location = block;
        this.zone = zoneName;
        this.name = name;

        this.fireAlarmSound = new FireAlarmSound();

        BlockData data = this.location.getBlock().getBlockData();
        if (data instanceof Lightable) {
            isLight = true;
        }
    }

    public SimpleStrobe(Block block, String name, String zoneName) {
        this(block.getLocation(), name, zoneName);
    }

    public void on() {
        if (isLight) {
            sound();
            BlockData data = this.location.getBlock().getBlockData();
            if (data instanceof Lightable) {
                ((Lightable) data).setLit(true);
                this.location.getBlock().setBlockData(data);
                isLight = true;
            } else isLight = false;
        } else {
            this.location.getBlock().setType(Material.REDSTONE_BLOCK);
        }
    }

    public void off() {
        if (isLight) {
            this.location.getBlock().setType(Material.REDSTONE_LAMP);
            return;
        }
        this.location.getBlock().setType(Material.SOUL_SAND);
    }

    public void sound() {
        this.location.getWorld().playSound(location, fireAlarmSound.getSound(), fireAlarmSound.getVolume(), fireAlarmSound.getPitch());
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public FireAlarmSound getFireAlarmSound() {
        return fireAlarmSound;
    }

    public void setFireAlarmSound(FireAlarmSound fireAlarmSound) {
        this.fireAlarmSound = fireAlarmSound;
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