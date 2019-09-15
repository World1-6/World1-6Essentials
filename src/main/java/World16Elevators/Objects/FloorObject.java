package World16Elevators.Objects;


import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("FloorObject")
public class FloorObject implements ConfigurationSerializable {

    private int floor;
    private Location atDoor;
    private BoundingBox boundingBox;
    private SignObject signObject;

    public FloorObject(int floor, Location atDoor, BoundingBox boundingBox) {
        this.floor = floor;
        this.atDoor = atDoor;
        this.boundingBox = boundingBox;
        this.signObject = null;
    }

    public FloorObject(int floor, Location atDoor) {
        this.floor = floor;
        this.atDoor = atDoor;
        this.boundingBox = null;
        this.signObject = null;
    }

    public FloorObject(int floor, Location atDoor, BoundingBox boundingBox, SignObject signObject) {
        this.floor = floor;
        this.atDoor = atDoor;
        this.boundingBox = boundingBox;
        this.signObject = signObject;
    }

    //GETTERS

    public int getFloor() {
        return floor;
    }

    public Location getAtDoor() {
        return atDoor;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public SignObject getSignObject() {
        return signObject;
    }

    public void setSignObject(SignObject signObject) {
        this.signObject = signObject;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("floor", floor);
        map.put("atDoor", atDoor);
        map.put("boundingBox", boundingBox);
        map.put("signObject", signObject);
        return map;
    }

    public static FloorObject deserialize(Map<String, Object> map) {
        return new FloorObject((int) map.get("floor"), (Location) map.get("atDoor"), (BoundingBox) map.get("boundingBox"), (SignObject) map.get("signObject"));
    }
}
