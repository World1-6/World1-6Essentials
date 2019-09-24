package World16FireAlarms;

public interface IFireAlarm {

    void registerNac();

    void registerZone(Zone zone);

    void registerStrobe(IStrobe iStrobe);

    void reset(Zone zone);

    void trouble();

    void alarm(Zone zone, TroubleReason troubleReason);
}