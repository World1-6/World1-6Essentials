package World16FireAlarms;

public class Zone {

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
}