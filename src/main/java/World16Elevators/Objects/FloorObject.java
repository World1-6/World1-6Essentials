package World16Elevators.Objects;


import World16.Utils.SimpleMath;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("FloorObject")
public class FloorObject implements ConfigurationSerializable {

    private int floor;

    private Location mainDoor;
    private List<Location> doorList;

    private BoundingBox boundingBox;
    private SignObject signObject;

    public FloorObject(int floor, Location atDoor, BoundingBox boundingBox) {
        this.floor = floor;

        this.doorList = new ArrayList<>();
        this.mainDoor = atDoor;

        this.boundingBox = boundingBox;
        this.signObject = null;
    }

    public FloorObject(int floor, Location atDoor) {
        this.floor = floor;

        this.doorList = new ArrayList<>();
        this.mainDoor = atDoor;

        this.boundingBox = null;
        this.signObject = null;
    }

    //USED FOR deserialize method.
    public FloorObject(int floor, Location mainDoor, List<Location> atDoor, BoundingBox boundingBox, SignObject signObject) {
        this.floor = floor;
        this.mainDoor = mainDoor;
        this.doorList = atDoor;
        this.boundingBox = boundingBox;
        this.signObject = signObject;
    }

    //GETTERS
    public int getFloor() {
        return floor;
    }

    public Location getMainDoor() {
        return mainDoor;
    }

    public List<Location> getDoorList() {
        return doorList;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public SignObject getSignObject() {
        return signObject;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setMainDoor(Location mainDoor) {
        this.mainDoor = mainDoor;
    }

    public void setDoorList(List<Location> doorList) {
        this.doorList = doorList;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void setSignObject(SignObject signObject) {
        this.signObject = signObject;
    }

    public static FloorObject from(ElevatorMovement elevatorMovement) {
        return new FloorObject(elevatorMovement.getFloor(), elevatorMovement.getAtDoor(), SimpleMath.toBoundingBox(elevatorMovement.getLocationDOWN().toVector(), elevatorMovement.getLocationUP().toVector()));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("floor", floor);
        map.put("mainDoor", mainDoor);
        map.put("doorList", doorList);
        map.put("boundingBox", boundingBox);
        map.put("signObject", signObject);
        return map;
    }

    public static FloorObject deserialize(Map<String, Object> map) {
        return new FloorObject((int) map.get("floor"), (Location) map.get("mainDoor"), (List<Location>) map.get("doorList"), (BoundingBox) map.get("boundingBox"), (SignObject) map.get("signObject"));
    }
}
