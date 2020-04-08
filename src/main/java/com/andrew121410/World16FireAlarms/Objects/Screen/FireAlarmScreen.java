package com.andrew121410.World16FireAlarms.Objects.Screen;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.SignUtils;
import com.andrew121410.World16.Utils.Translate;
import com.andrew121410.World16FireAlarms.interfaces.IFireAlarm;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SerializableAs("FireAlarmScreen")
public class FireAlarmScreen implements ConfigurationSerializable {

    private Main plugin;

    private String name;
    private String fireAlarmName;

    private Location location;

    private FireAlarmSignOS fireAlarmSignOS;

    private int line = -0;
    private int scroll = 0;

    private int min = 0;
    private int max = 3;

    private SignCache signCache1;

    private boolean hasTextChanged = false;
    private boolean hasScrollChanged = false;

    private boolean isTickerRunning = false;
    private boolean stop = false;

    private Map<String, IFireAlarm> fireAlarmMap;

    private List<List<String>> partition = null;

    public FireAlarmScreen(Main plugin, String name, String fireAlarmName, Location location) {
        this.plugin = plugin;
        this.name = name;
        this.fireAlarmName = fireAlarmName;
        this.location = location;

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();

        this.fireAlarmSignOS = new FireAlarmSignOS(this.plugin, FireAlarmSignMenu.OFF, this.name, this.fireAlarmName);

        Sign sign = SignUtils.isSign(location.getBlock());
        if (sign != null) {
            tick();
            this.fireAlarmSignOS.loadFirstTime(this, sign);
        }
    }

    public FireAlarmScreen(Main plugin, String name, String fireAlarmName, Location location, FireAlarmSignOS fireAlarmSignOS) {
        this.plugin = plugin;
        this.name = name;
        this.fireAlarmName = fireAlarmName;
        this.location = location;

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();

        this.fireAlarmSignOS = fireAlarmSignOS;
    }

    public void onClick(Player player) {
        if (!this.isTickerRunning) {
            tick(player);
            return;
        }

        Sign sign = SignUtils.isSign(location.getBlock());
        if (sign != null) {
            this.fireAlarmSignOS.onLine(this, player, sign, line, scroll);
        }
    }

    public void onScroll(Player player, boolean up) {
        if (!this.isTickerRunning) {
            tick(player);
            return;
        }

        if (this.partition == null) {
            return;
        }

        Sign sign = SignUtils.isSign(location.getBlock());
        if (sign != null) {
            if (up) {
                int upONE = this.scroll + 1;
                if (upONE < this.partition.size()) {
                    this.scroll++;
                    for (int i = 0; i <= 3; i++) {
                        String line;
                        if (i < this.partition.get(this.scroll).size()) {
                            line = this.partition.get(this.scroll).get(i);
                        } else line = "";
                        sign.setLine(i, line);
                    }
                    this.signCache1 = new SignCache(sign);
                    this.hasScrollChanged = true;
                }
            } else {
                int downONE = this.scroll - 1;
                if (downONE >= 0 && downONE <= this.partition.size()) {
                    this.scroll--;
                    for (int i = 0; i <= 3; i++) {
                        String line;
                        if (i < this.partition.get(this.scroll).size()) {
                            line = this.partition.get(this.scroll).get(i);
                        } else line = "";
                        sign.setLine(i, line);
                    }
                    this.signCache1 = new SignCache(sign);
                    this.hasScrollChanged = true;
                }
            }
        }
    }

    public void changeLines(Player player) {
        if (!this.isTickerRunning) {
            tick(player);
            return;
        }

        if (this.min <= this.line && this.line < this.max) {
            line++;
        } else {
            line = min;
        }
    }

    public void tick(Player player) {
        if (!this.isTickerRunning) {
            tick();
            player.sendMessage(Translate.chat("&2Sign turned on."));
        }
    }

    private void tick() {
        if (!this.isTickerRunning) {
            this.isTickerRunning = true;
            Sign sign = SignUtils.isSign(location.getBlock());
            if (sign != null) {
                String first = ">";
                String last = "<";
                new BukkitRunnable() {
                    int temp = 0;
                    String text = sign.getLine(line);
                    SignCache signCache = new SignCache(sign);
                    int oldLine = line;

                    @Override
                    public void run() {
                        if (stop && temp == 0) {
                            isTickerRunning = false;
                            stop = false;
                            this.cancel();
                            return;
                        }

                        if (temp == 0 && !hasTextChanged && !hasScrollChanged) {
                            oldLine = line;
                            signCache.fromSign(sign);
                            text = sign.getLine(line);
                            sign.setLine(line, first + text);
                            if (!sign.update()) stop = true;
                            temp++;
                        } else if (temp > 0 && !hasTextChanged && !hasScrollChanged) {
                            sign.setLine(oldLine, text);
                            if (!sign.update()) stop = true;
                            temp = 0;
                        } else if (hasTextChanged) {
                            signCache1.update(sign);
                            partition = null;
                            oldLine = line;
                            temp = 0;
                            hasTextChanged = false;
                        } else if (hasScrollChanged) {
                            signCache1.update(sign);
                            oldLine = line;
                            temp = 0;
                            hasScrollChanged = false;
                        }
                    }
                }.runTaskTimer(this.plugin, 10L, 10L);
            }
        }
    }

    public void updateSign(Sign sign) {
        tick();
        this.signCache1 = new SignCache(sign);
        this.hasTextChanged = true;
    }

    public void updateSign(Sign sign, List<String> stringList) {
        tick();
        this.partition = Lists.partition(stringList, 4);
        for (int i = 0; i <= 3; i++) {
            String line;
            if (i < this.partition.get(0).size()) {
                line = this.partition.get(0).get(i);
            } else line = "";
            sign.setLine(i, line);
        }
        this.signCache1 = new SignCache(sign);
        this.hasScrollChanged = true;
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

    public String getFireAlarmName() {
        return fireAlarmName;
    }

    public void setFireAlarmName(String fireAlarmName) {
        this.fireAlarmName = fireAlarmName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public FireAlarmSignOS getFireAlarmSignOS() {
        return fireAlarmSignOS;
    }

    public void setFireAlarmSignOS(FireAlarmSignOS fireAlarmSignOS) {
        this.fireAlarmSignOS = fireAlarmSignOS;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getScroll() {
        return scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public SignCache getSignCache1() {
        return signCache1;
    }

    public void setSignCache1(SignCache signCache1) {
        this.signCache1 = signCache1;
    }

    public boolean isHasTextChanged() {
        return hasTextChanged;
    }

    public void setHasTextChanged(boolean hasTextChanged) {
        this.hasTextChanged = hasTextChanged;
    }

    public boolean isTickerRunning() {
        return isTickerRunning;
    }

    public void setTickerRunning(boolean tickerRunning) {
        isTickerRunning = tickerRunning;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public Map<String, IFireAlarm> getFireAlarmMap() {
        return fireAlarmMap;
    }

    public void setFireAlarmMap(Map<String, IFireAlarm> fireAlarmMap) {
        this.fireAlarmMap = fireAlarmMap;
    }

    public List<List<String>> getPartition() {
        return partition;
    }

    public void setPartition(List<List<String>> partition) {
        this.partition = partition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FireAlarmScreen that = (FireAlarmScreen) o;
        return line == that.line &&
                scroll == that.scroll &&
                min == that.min &&
                max == that.max &&
                hasTextChanged == that.hasTextChanged &&
                isTickerRunning == that.isTickerRunning &&
                stop == that.stop &&
                Objects.equals(plugin, that.plugin) &&
                Objects.equals(name, that.name) &&
                Objects.equals(fireAlarmName, that.fireAlarmName) &&
                Objects.equals(location, that.location) &&
                Objects.equals(fireAlarmSignOS, that.fireAlarmSignOS) &&
                Objects.equals(signCache1, that.signCache1) &&
                Objects.equals(fireAlarmMap, that.fireAlarmMap) &&
                Objects.equals(partition, that.partition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugin, name, fireAlarmName, location, fireAlarmSignOS, line, scroll, min, max, signCache1, hasTextChanged, isTickerRunning, stop, fireAlarmMap, partition);
    }

    @Override
    public String toString() {
        return "FireAlarmScreen{" +
                "plugin=" + plugin +
                ", name='" + name + '\'' +
                ", fireAlarmName='" + fireAlarmName + '\'' +
                ", location=" + location +
                ", fireAlarmSignOS=" + fireAlarmSignOS +
                ", line=" + line +
                ", scroll=" + scroll +
                ", min=" + min +
                ", max=" + max +
                ", signCache1=" + signCache1 +
                ", hasTextChanged=" + hasTextChanged +
                ", isTickerRunning=" + isTickerRunning +
                ", stop=" + stop +
                ", fireAlarmMap=" + fireAlarmMap +
                ", partition=" + partition +
                '}';
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", this.name);
        map.put("FireAlarmName", this.fireAlarmName);
        map.put("Location", this.location);
        map.put("FireAlarmSignScreen", this.fireAlarmSignOS);
        return map;
    }

    public static FireAlarmScreen deserialize(Map<String, Object> map) {
        return new FireAlarmScreen(Main.getPlugin(), (String) map.get("Name"), (String) map.get("FireAlarmName"), (Location) map.get("Location"), (FireAlarmSignOS) map.get("FireAlarmSignScreen"));
    }
}
