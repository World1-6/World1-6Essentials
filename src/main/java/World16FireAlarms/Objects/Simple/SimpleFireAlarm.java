package World16FireAlarms.Objects.Simple;

import World16.Main.Main;
import World16FireAlarms.Objects.FireAlarmStatus;
import World16FireAlarms.Objects.TroubleReason;
import World16FireAlarms.Objects.Zone;
import World16FireAlarms.Screen.FireAlarmScreen;
import World16FireAlarms.interfaces.IFireAlarm;
import World16FireAlarms.interfaces.IStrobe;
import org.bukkit.Location;
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
    private Map<String, Location> signsMap;

    private FireAlarmStatus fireAlarmStatus;

    public SimpleFireAlarm(Main plugin, String name) {
        this.plugin = plugin;

        this.name = name;

        //Maps / Sets
        this.strobesMap = new HashMap<>();
        this.zones = new ArrayList<>();
        this.signsMap = new HashMap<>();

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

    public void registerSign(String string, Location location) {
        this.signsMap.put(string.toLowerCase(), location);
    }

    public void reset(Optional<Zone> zone) {
        if (!zone.isPresent()) {
            this.fireAlarmStatus = FireAlarmStatus.READY;
            this.resetStrobes();
            for (Map.Entry<String, Location> entry : this.signsMap.entrySet()) {
                String k = entry.getKey();
                Location v = entry.getValue();

                FireAlarmScreen fireAlarmScreen = this.plugin.getSetListMap().getFireAlarmScreenMap().get(v);
                if (fireAlarmScreen != null) {
                    this.plugin.getSetListMap().getFireAlarmScreenMap().get(v).getFireAlarmSignScreen().resetSign(fireAlarmScreen, fireAlarmScreen.getSign(), true);
                } else {
                    this.plugin.getFireAlarmManager().deleteFireAlarmSignScreen(this.name, k.toLowerCase(), v);
                }
            }
        }
    }

    public void trouble() {

    }

    public void alarm(Optional<Zone> zone, TroubleReason troubleReason) {
        if (!zone.isPresent()) {
            this.fireAlarmStatus = FireAlarmStatus.ALARM;
            setupMarchTime();
            for (Map.Entry<String, Location> entry : this.signsMap.entrySet()) {
                String k = entry.getKey();
                Location v = entry.getValue();

                FireAlarmScreen fireAlarmScreen = this.plugin.getSetListMap().getFireAlarmScreenMap().get(v);
                if (fireAlarmScreen != null)
                    this.plugin.getSetListMap().getFireAlarmScreenMap().get(v).getFireAlarmSignScreen().sendPopup(troubleReason, zone, fireAlarmScreen, fireAlarmScreen.getSign());
                else {
                    this.plugin.getFireAlarmManager().deleteFireAlarmSignScreen(this.name, k.toLowerCase(), v);
                }
            }
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
                        for (Map.Entry<String, IStrobe> entry : strobesMap.entrySet()) {
                            String k = entry.getKey();
                            IStrobe v = entry.getValue();
                            v.on();
                        }
                        marchTime++;
                    } else if (marchTime >= 1) {
                        for (Map.Entry<String, IStrobe> entry : strobesMap.entrySet()) {
                            String k = entry.getKey();
                            IStrobe v = entry.getValue();
                            v.off();
                        }
                        marchTime = 0;
                    }
                } else {
                    fireAlarmStatus = FireAlarmStatus.READY;
                    resetStrobes();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 10L, 10L);
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

    @Override
    public Map<String, IStrobe> getStrobesMap() {
        return strobesMap;
    }

    public void setStrobesMap(Map<String, IStrobe> strobesMap) {
        this.strobesMap = strobesMap;
    }

    @Override
    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public Map<String, Location> getSigns() {
        return signsMap;
    }

    public void setSignsMap(Map<String, Location> signsMap) {
        this.signsMap = signsMap;
    }

    public FireAlarmStatus getFireAlarmStatus() {
        return fireAlarmStatus;
    }

    public void setFireAlarmStatus(FireAlarmStatus fireAlarmStatus) {
        this.fireAlarmStatus = fireAlarmStatus;
    }

    public int getMarchTime() {
        return marchTime;
    }

    public void setMarchTime(int marchTime) {
        this.marchTime = marchTime;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", this.name);
        return map;
    }

    public static SimpleFireAlarm deserialize(Map<String, Object> map) {
        return new SimpleFireAlarm(Main.getPlugin(), (String) map.get("Name"));
    }
}