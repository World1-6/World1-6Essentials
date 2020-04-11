package com.andrew121410.World16FireAlarms.interfaces;

import com.andrew121410.World16FireAlarms.FireAlarmSound;
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
