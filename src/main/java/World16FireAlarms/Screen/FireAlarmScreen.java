package World16FireAlarms.Screen;

import World16.Main.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

public class FireAlarmScreen {

    private Main plugin;

    private String name;
    private Location location;

    private FireScreenTech fireScreenTech;

    private int line = 0;

    public FireAlarmScreen(Main plugin, String name, Location location) {
        this.plugin = plugin;
        this.name = name;
        this.location = location;

        this.fireScreenTech = new FireScreenTech(this.plugin);
    }

    public void onClick() {
        if (getSign() != null) {
            this.fireScreenTech.onLine(getSign(), line);
        }
    }

    public void changeLines() {
        if (line < 3) {
            line++;
        } else {
            line = 0;
        }
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public FireScreenTech getFireScreenTech() {
        return fireScreenTech;
    }

    public void setFireScreenTech(FireScreenTech fireScreenTech) {
        this.fireScreenTech = fireScreenTech;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
