package World16FireAlarms.interfaces;

import World16FireAlarms.Objects.FireAlarmReason;
import World16FireAlarms.Objects.FireAlarmSound;
import World16FireAlarms.Objects.FireAlarmTempo;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

public interface IFireAlarm extends ConfigurationSerializable {

    void registerNac();

    void registerStrobe(IStrobe iStrobe);

    void registerSign(String string, Location location);

    void reset();

    void trouble();

    void alarm(FireAlarmReason fireAlarmReason);

    Map<String, IStrobe> getStrobesMap();

    Map<String, Location> getSigns();

    void setFireAlarmSound(FireAlarmSound fireAlarmSound);

    FireAlarmSound getFireAlarmSound();

    void setFireAlarmTempo(FireAlarmTempo fireAlarmTemp);

    FireAlarmTempo getFireAlarmTempo();
}