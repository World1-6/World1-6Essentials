package World16FireAlarms.interfaces;

import World16FireAlarms.Objects.TroubleReason;
import World16FireAlarms.Objects.Zone;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IFireAlarm extends ConfigurationSerializable {

    void registerNac();

    void registerZone(Zone zone);

    void registerStrobe(IStrobe iStrobe);

    void registerSign(Location location);

    void reset(Optional<Zone> zone);

    void trouble();

    void alarm(Optional<Zone> zone, TroubleReason troubleReason);

    Map<String, IStrobe> getStrobesMap();

    List<Zone> getZones();

    List<Location> getSigns();
}