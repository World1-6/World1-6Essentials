package World16FireAlarms.Objects.Simple;

import World16.Main.Main;
import World16.Utils.SignUtils;
import World16FireAlarms.Objects.*;
import World16FireAlarms.Objects.Screen.FireAlarmScreen;
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
    private boolean isAlarmCurrently = false;

    private FireAlarmSound fireAlarmSound;
    private FireAlarmTempo fireAlarmTempo;

    public SimpleFireAlarm(Main plugin, String name, FireAlarmSound fireAlarmSound, FireAlarmTempo fireAlarmTempo) {
        this.plugin = plugin;

        this.name = name;

        this.fireAlarmSound = fireAlarmSound;
        this.fireAlarmTempo = fireAlarmTempo;

        //Maps / Sets
        this.strobesMap = new HashMap<>();
        this.zones = new ArrayList<>();
        this.signsMap = new HashMap<>();

        this.fireAlarmStatus = FireAlarmStatus.READY;
    }

    public void registerStrobe(IStrobe iStrobe) {
        this.strobesMap.putIfAbsent(iStrobe.getName(), iStrobe);
        this.strobesMap.get(iStrobe.getName()).setFireAlarmSound(fireAlarmSound);
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
        this.fireAlarmStatus = FireAlarmStatus.READY;

        //Signs
        for (Map.Entry<String, Location> entry : this.signsMap.entrySet()) {
            String k = entry.getKey();
            Location v = entry.getValue();

            FireAlarmScreen fireAlarmScreen = this.plugin.getSetListMap().getFireAlarmScreenMap().get(v);
            if (fireAlarmScreen != null)
                fireAlarmScreen.getFireAlarmSignOS().resetSign(fireAlarmScreen, SignUtils.isSign(fireAlarmScreen.getLocation().getBlock()), true);
            else {
                //Wait 1 second before removing so it won't cause a ConcurrentModificationException
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getFireAlarmManager().deleteFireAlarmSignScreen(name, k.toLowerCase(), v);
                    }
                }.runTaskLater(plugin, 20L);
            }
        }
        //SIGNS DONE...
    }

    public void trouble() {
        //TODO add Trouble
    }

    public void alarm(FireAlarmReason fireAlarmReason) {
        this.fireAlarmStatus = FireAlarmStatus.ALARM;

        if (fireAlarmTempo == FireAlarmTempo.CODE3) setupCode3();
        else if (fireAlarmTempo == FireAlarmTempo.MARCH_TIME) setupMarchTime();

        //Signs
        for (Map.Entry<String, Location> entry : this.signsMap.entrySet()) {
            String k = entry.getKey();
            Location v = entry.getValue();

            FireAlarmScreen fireAlarmScreen = this.plugin.getSetListMap().getFireAlarmScreenMap().get(v);
            if (fireAlarmScreen != null)
                fireAlarmScreen.getFireAlarmSignOS().sendPopup(fireAlarmScreen, SignUtils.isSign(fireAlarmScreen.getLocation().getBlock()), fireAlarmReason);
            else {
                //Wait 1 second before removing so it won't cause a ConcurrentModificationException
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getFireAlarmManager().deleteFireAlarmSignScreen(name, k.toLowerCase(), v);
                    }
                }.runTaskLater(plugin, 20L);
            }
        }
        //Signs DONE...
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
        if (!isAlarmCurrently) {
            isAlarmCurrently = true;

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
                        isAlarmCurrently = false;
                        marchTime = 0;
                        resetStrobes();
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 10L, 10L);
        }
    }

    private int code3 = 0;

    private void setupCode3() {
        if (!this.isAlarmCurrently) {
            this.isAlarmCurrently = true;

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (fireAlarmStatus == FireAlarmStatus.ALARM) {
                        if (code3 == 0 || code3 == 2 || code3 == 4) {
                            for (Map.Entry<String, IStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                IStrobe v = entry.getValue();
                                v.on();
                            }
                            code3++;
                        } else if (code3 == 1 || code3 == 3 || code3 == 5) {
                            for (Map.Entry<String, IStrobe> entry : strobesMap.entrySet()) {
                                String k = entry.getKey();
                                IStrobe v = entry.getValue();
                                v.off();
                            }
                            code3++;
                        } else if (code3 >= 6) {
                            if (code3 == 8) {
                                code3 = 0;
                                return;
                            }
                            code3++;
                        }
                    } else {
                        isAlarmCurrently = false;
                        code3 = 0;
                        resetStrobes();
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 10L, 10L);
        }
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

    @Override
    public void setFireAlarmSound(FireAlarmSound fireAlarmSound) {
        this.fireAlarmSound = fireAlarmSound;
        for (Map.Entry<String, IStrobe> entry : this.strobesMap.entrySet()) {
            String k = entry.getKey();
            IStrobe v = entry.getValue();

            v.setFireAlarmSound(fireAlarmSound);
        }
    }

    @Override
    public FireAlarmSound getFireAlarmSound() {
        return this.fireAlarmSound;
    }

    @Override
    public void setFireAlarmTempo(FireAlarmTempo fireAlarmTemp) {
        this.fireAlarmTempo = fireAlarmTemp;
    }

    @Override
    public FireAlarmTempo getFireAlarmTempo() {
        return this.fireAlarmTempo;
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
        map.put("FireAlarmSound", this.fireAlarmSound);
        map.put("FireAlarmTempo", this.fireAlarmTempo.toString());
        return map;
    }

    public static SimpleFireAlarm deserialize(Map<String, Object> map) {
        return new SimpleFireAlarm(Main.getPlugin(), (String) map.get("Name"), (FireAlarmSound) map.get("FireAlarmSound"), FireAlarmTempo.valueOf((String) map.get("FireAlarmTempo")));
    }
}