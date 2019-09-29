package World16FireAlarms.Screen;

import World16.Main.Main;
import World16FireAlarms.Objects.FireAlarmSignMenu;
import World16FireAlarms.interfaces.IFireAlarm;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("FireAlarmScreen")
public class FireAlarmScreen implements ConfigurationSerializable {

    private Main plugin;

    private String name;
    private String fireAlarmName;

    private Location location;

    private FireAlarmSignScreen fireAlarmSignScreen;

    private int line = -0;

    private int min = 0;
    private int max = 3;

    private SignCache signCache1;
    private boolean hasTextChanged = false;

    private boolean isTickerRunning = false;
    private boolean stop = false;

    private Map<String, IFireAlarm> fireAlarmMap;

    public FireAlarmScreen(Main plugin, String name, String fireAlarmName, Location location) {
        this.plugin = plugin;
        this.name = name;
        this.fireAlarmName = fireAlarmName;
        this.location = location;

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();

        this.fireAlarmSignScreen = new FireAlarmSignScreen(this.plugin, FireAlarmSignMenu.OFF, this.name, this.fireAlarmName);
    }

    public FireAlarmScreen(Main plugin, String name, String fireAlarmName, Location location, FireAlarmSignScreen fireAlarmSignScreen) {
        this.plugin = plugin;
        this.name = name;
        this.fireAlarmName = fireAlarmName;
        this.location = location;

        this.fireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();

        this.fireAlarmSignScreen = fireAlarmSignScreen;
    }

    public void onClick(Player player) {
        Sign sign = getSign();
        if (sign != null) {
            this.fireAlarmSignScreen.onLine(this, player, sign, line);
        }
    }

    public void changeLines() {
        if (this.min <= this.line && this.line < this.max && this.isTickerRunning) {
            line++;
        } else {
            line = min;
        }

        if (!this.isTickerRunning)
            tick();
    }

    private void tick() {
        if (!this.isTickerRunning) {
            this.isTickerRunning = true;
            Sign sign = getSign();
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
                        if (stop) {
                            this.cancel();
                            return;
                        }

                        if (temp == 0 && !hasTextChanged) {
                            oldLine = line;
                            signCache.fromSign(sign);
                            text = sign.getLine(line);
                            sign.setLine(line, first + text);
                            if (!sign.update()) stop = true;
                            temp++;
                        } else if (temp > 0 && !hasTextChanged) {
                            sign.setLine(oldLine, text);
                            if (!sign.update()) stop = true;
                            temp = 0;
                        } else if (hasTextChanged) {
                            signCache1.update(sign);
                            oldLine = line;
                            temp = 0;
                            hasTextChanged = false;
                        }
                    }
                }.runTaskTimer(this.plugin, 10L, 10L);
            }
        }
    }

    public void updateSign(Sign sign) {
        this.signCache1 = new SignCache(sign);
        this.hasTextChanged = true;
    }

    public boolean isSign() {
        if (location == null) {
            return false;
        }

        return location.getBlock().getType() == Material.WALL_SIGN || location.getBlock().getType() == Material.SIGN_POST;
    }

    public Sign getSign() {
        if (isSign()) {
            return (Sign) location.getBlock().getState();
        }
        return null;
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

    public FireAlarmSignScreen getFireAlarmSignScreen() {
        return fireAlarmSignScreen;
    }

    public void setFireAlarmSignScreen(FireAlarmSignScreen fireAlarmSignScreen) {
        this.fireAlarmSignScreen = fireAlarmSignScreen;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
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

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", this.name);
        map.put("FireAlarmName", this.fireAlarmName);
        map.put("Location", this.location);
        map.put("FireAlarmSignScreen", this.fireAlarmSignScreen);
        return map;
    }

    public static FireAlarmScreen deserialize(Map<String, Object> map) {
        return new FireAlarmScreen(Main.getPlugin(), (String) map.get("Name"), (String) map.get("FireAlarmName"), (Location) map.get("Location"), (FireAlarmSignScreen) map.get("FireAlarmSignScreen"));
    }
}
