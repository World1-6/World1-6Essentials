package World16FireAlarms.interfaces;

public interface IFireAlarm {

    void registerStrobe();

    void registerNac();

    void registerZone();

    void alarm();

    void trouble();

    void reset();

}
