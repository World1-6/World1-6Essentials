package World16FireAlarms;

import org.bukkit.Location;

public interface IStrobe {

    void on();

    void off();

    void sound();

    String getName();

    Zone getZone();

    Location getLocation();
}