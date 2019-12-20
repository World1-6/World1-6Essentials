package World16TrafficLights.Objects;

import org.bukkit.Location;
import org.bukkit.Material;

public class TrafficLight {

    private Location location;

    public TrafficLight(Location location) {
        this.location = location;
    }

    public boolean doLight(TrafficLightState trafficLightState) {
        switch (trafficLightState) {
            case GREEN:
                return Green();
            case YELLOW:
                return Yellow();
            case RED:
                return Red();
        }
        return false;
    }

    public boolean Green() {
        location.getBlock().setType(Material.GREEN_WOOL);
        return true;
    }

    public boolean Yellow() {
        location.getBlock().setType(Material.YELLOW_WOOL);
        return true;
    }

    public boolean Red() {
        location.getBlock().setType(Material.RED_WOOL);
        return true;
    }
}
