package World16Elevators.Objects;

import World16.Main.Main;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public void callElevatorClosest(int floorNum, ElevatorStatus elevatorStatus, ElevatorWho elevatorWho) {
        ElevatorObject elevatorObject = getClosestElevator(floorNum, true, elevatorStatus);
        elevatorObject.goToFloor(floorNum, elevatorStatus, elevatorWho);
    }

    public void callAllElevators(int floorNum, ElevatorStatus elevatorStatus, ElevatorWho elevatorWho) {
        this.elevatorsMap.forEach((k, v) -> v.goToFloor(floorNum, elevatorStatus, elevatorWho));
    }

    public ElevatorObject getClosestElevator(int floorNumber, boolean smart, ElevatorStatus elevatorStatus) {
        Optional<ElevatorObject> optionalElevatorObject = this.elevatorsMap.values().stream().min(Comparator.comparingInt(i -> Math.abs(i.getElevatorMovement().getAtDoor().getBlockY() - i.getFloor(floorNumber).getMainDoor().getBlockY())));
        if (smart) {
            if (elevatorStatus == ElevatorStatus.DONT_KNOW) return optionalElevatorObject.get();

            Optional<ElevatorObject> s1 = this.elevatorsMap.values().stream().filter(elevatorObject -> elevatorObject.isGoing() && elevatorObject.getStopBy().toElevatorStatus() == elevatorStatus && elevatorObject.getFloorBuffer().contains(floorNumber)).min(Comparator.comparingInt(i -> Math.abs(i.getElevatorMovement().getAtDoor().getBlockY() - i.getFloor(floorNumber).getMainDoor().getBlockY())));
            return s1.orElseGet(optionalElevatorObject::get);
        }

        return optionalElevatorObject.get();
    }

    public ElevatorObject getElevator(String name) {
        return elevatorsMap.get(name.toLowerCase());
    }

    public void registerElevator(String name, ElevatorObject elevatorObject) {
        elevatorObject.setElevatorControllerName(this.controllerName);
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
