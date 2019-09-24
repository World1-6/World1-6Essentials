package World16FireAlarms;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IFireAlarm extends ConfigurationSerializable {

    void registerNac();

    void registerZone(Zone zone);

    void registerStrobe(IStrobe iStrobe);

    void reset(Optional<Zone> zone);

    void trouble();

    void alarm(Optional<Zone> zone, TroubleReason troubleReason);

    Map<String, IStrobe> getStrobesMap();

    public List<Zone> getZones();
}