package World16FireAlarms.interfaces;

import World16FireAlarms.Objects.FireAlarmSound;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface IStrobe extends ConfigurationSerializable {

    void on();

    void off();

    void sound();

    String getName();

    Location getLocation();

    void setFireAlarmSound(FireAlarmSound fireAlarmSound);
}
