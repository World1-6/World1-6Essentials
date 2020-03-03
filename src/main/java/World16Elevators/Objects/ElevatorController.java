package World16Elevators.Objects;

import World16.Main.Main;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("ElevatorController")
public class ElevatorController implements ConfigurationSerializable {

    private Main plugin;

    private String controllerName;
    private Map<String, ElevatorObject> elevatorsMap;

    public ElevatorController(Main plugin, String controllerName) {
        this.plugin = plugin;
        this.controllerName = controllerName;

        this.elevatorsMap = new HashMap<>();
    }

    //    @TODO Make this actually work
    public ElevatorObject getClosestElevator(int floorNumber) {
        return null;
    }

    public ElevatorObject getElevator(String name) {
        return elevatorsMap.get(name.toLowerCase());
    }

    public void registerElevator(String name, ElevatorObject elevatorObject) {
        this.elevatorsMap.putIfAbsent(name.toLowerCase(), elevatorObject);
    }

    public Map<String, ElevatorObject> getElevatorsMap() {
        return this.elevatorsMap;
    }

    public boolean isSingle() {
        return this.elevatorsMap.size() == 0;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("ControllerName", this.controllerName);
        return map;
    }

    public static ElevatorController deserialize(Map<String, Object> map) {
        return new ElevatorController(Main.getPlugin(), (String) map.get("ControllerName"));
    }
}
