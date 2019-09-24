package World16FireAlarms;

import World16.Main.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

public class SimpleFireAlarm implements IFireAlarm {

    private Main plugin;

    private String name;

    private Map<String, IStrobe> strobesMap;
    private List<Zone> zones;

    public SimpleFireAlarm(Main plugin) {
        this.plugin = plugin;

        //Maps / Sets
        this.strobesMap = new HashMap<>();
        this.zones = new ArrayList<>();
    }

    public void registerStrobe(IStrobe iStrobe) {
        this.strobesMap.putIfAbsent(iStrobe.getName(), iStrobe);
    }

    public void registerZone(Zone zone) {
        this.zones.add(zone);
    }

    public void registerNac() {

    }

    public void reset(Zone zone) {

    }

    public void trouble() {

    }

    public void alarm(Zone zone, TroubleReason troubleReason) {

    }
}