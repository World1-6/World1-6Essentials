package World16Elevators.Objects;


import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ElevatorMovement implements ConfigurationSerializable {

    private Integer floor;

    private Location atDoor;
    private Location locationDOWN;
    private Location locationUP;

    public ElevatorMovement(Integer floor, Location atDoor, Location locationDOWN, Location locationUP) {
        this.floor = floor;
        this.atDoor = atDoor;
        this.locationDOWN = locationDOWN;
        this.locationUP = locationUP;
    }

    public void moveUP() {
        this.atDoor.add(0, 1, 0);
        this.locationUP.add(0, 1, 0);
        this.locationDOWN.add(0, 1, 0);
    }

    public void moveDOWN() {
        this.atDoor.subtract(0, 1, 0);
        this.locationUP.subtract(0, 1, 0);
        this.locationDOWN.subtract(0, 1, 0);
    }

    //GETTERS AND SETTERS
    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Location getAtDoor() {
        return atDoor;
    }

    public void setAtDoor(Location atDoor) {
        this.atDoor = atDoor;
    }

    public Location getLocationDOWN() {
        return locationDOWN;
    }

    public void setLocationDOWN(Location locationDOWN) {
        this.locationDOWN = locationDOWN;
    }

    public Location getLocationUP() {
        return locationUP;
    }

    public void setLocationUP(Location locationUP) {
        this.locationUP = locationUP;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("floor", floor);
        map.put("atDoor", atDoor);
        map.put("locationDOWN", this.locationUP);
        map.put("locationUP", this.locationDOWN);
        return map;
    }

    public static ElevatorMovement deserialize(Map<String, Object> map) {
        return new ElevatorMovement((Integer) map.get("floor"), (Location) map.get("atDoor"), (Location) map.get("locationDOWN"), (Location) map.get("locationUP"));
    }

    //JAVA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElevatorMovement that = (ElevatorMovement) o;
        return Objects.equals(floor, that.floor) &&
                Objects.equals(atDoor, that.atDoor) &&
                Objects.equals(locationDOWN, that.locationDOWN) &&
                Objects.equals(locationUP, that.locationUP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, atDoor, locationDOWN, locationUP);
    }

    @Override
    public String toString() {
        return "ElevatorMovement{" +
                "floor=" + floor +
                ", atDoor=" + atDoor +
                ", locationDOWN=" + locationDOWN +
                ", locationUP=" + locationUP +
                '}';
    }
}
