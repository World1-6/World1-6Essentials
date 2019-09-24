package World16FireAlarms;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface IStrobe extends ConfigurationSerializable {

    void on();

    void off();

    void sound();

    String getName();

    Location getLocation();
}