package World16FireAlarms.Objects.Simple;

import World16.Main.Main;
import World16FireAlarms.Objects.FireAlarmStatus;
import World16FireAlarms.Objects.TroubleReason;
import World16FireAlarms.Objects.Zone;
import World16FireAlarms.interfaces.IFireAlarm;
import World16FireAlarms.interfaces.IStrobe;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@SerializableAs("IFireAlarm")
public class SimpleFireAlarm implements IFireAlarm, ConfigurationSerializable {

    private Main plugin;

    private String name;

    private Map<String, IStrobe> strobesMap;
    private List<Zone> zones;

    private FireAlarmStatus fireAlarmStatus;

    public SimpleFireAlarm(String name) {
        this.name = name;

        //Maps / Sets
        this.strobesMap = new HashMap<>();
        this.zones = new ArrayList<>();
    }

    public SimpleFireAlarm(Main plugin) {
        this.plugin = plugin;

        //Maps / Sets
        this.strobesMap = new HashMap<>();
        this.zones = new ArrayList<>();

        this.fireAlarmStatus = FireAlarmStatus.READY;
    }

    public void registerStrobe(IStrobe iStrobe) {
        this.strobesMap.putIfAbsent(iStrobe.getName(), iStrobe);
    }

    public void registerZone(Zone zone) {
        this.zones.add(zone);
    }

    public void registerNac() {

    }

    public void reset(Optional<Zone> zone) {
        if (!zone.isPresent()) {
            this.fireAlarmStatus = FireAlarmStatus.READY;
            this.resetStrobes();
        }
    }

    public void trouble() {

    }

    public void alarm(Optional<Zone> zone, TroubleReason troubleReason) {
        if (!zone.isPresent()) {
            this.fireAlarmStatus = FireAlarmStatus.ALARM;
            setupMarchTime();
        }
    }

    private void resetStrobes() {
        for (Map.Entry<String, IStrobe> entry : this.strobesMap.entrySet()) {
            String k = entry.getKey();
            IStrobe v = entry.getValue();
            v.off();
        }
    }

    private int marchTime = 0;

    private void setupMarchTime() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (fireAlarmStatus == FireAlarmStatus.ALARM) {
                    if (marchTime == 0) {
                        strobesMap.forEach((k, v) -> v.on());
                        marchTime++;
                    } else if (marchTime >= 1) {
                        strobesMap.forEach((k, v) -> v.off());
                        marchTime = 0;
                    }
                } else {
                    fireAlarmStatus = FireAlarmStatus.READY;
                    resetStrobes();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 15L, 15L);
    }

    //GETTERS AND SETTERS
    public Main getPlugin() {
        return plugin;
    }

    public void setPlugin(Main plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, IStrobe> getStrobesMap() {
        return strobesMap;
    }

    public void setStrobesMap(Map<String, IStrobe> strobesMap) {
        this.strobesMap = strobesMap;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public FireAlarmStatus getFireAlarmStatus() {
        return fireAlarmStatus;
    }

    public void setFireAlarmStatus(FireAlarmStatus fireAlarmStatus) {
        this.fireAlarmStatus = fireAlarmStatus;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", this.name);
        return map;
    }

    public static SimpleFireAlarm deserialize(Map<String, Object> map) {
        return new SimpleFireAlarm((String) map.get("Name"));
    }
}