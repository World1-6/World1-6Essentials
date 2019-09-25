package World16FireAlarms.Screen;

import World16.Commands.elevator;
import World16.Commands.sign;
import World16.Main.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

public class FireAlarmScreen {

    private Main plugin;

    private String name;
    private Location location;

    private FireScreenTech fireScreenTech;

    private int line = 0;
    private boolean hasLineChanged = false;

    private boolean isTickerRunning = false;

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

        if(this.isTickerRunning == true){
        this.hasLineChanged = true;
        }else{
            tick();
        }
    }

    private void tick(){
        if(!isTickerRunning){
        Sign sign = getSign();
        if(sign != null){
            String text = sign.getLine(this.line);
            String first = "> ";
            String last = " <";
            int oldLine = this.line;
            new BukkitRunnable(){
                int temp = 0;
                @Override
                public void run() {
                    if(temp == 0 && !hasLineChanged){
                    sign.setLine(oldLine, first + text + last);
                    temp++;
                    }else if(temp > 0 && !hasLineChanged){
                        sign.setLine(oldLine, text);
                        sign.update();
                        temp = 0;
                    }else if (hasLineChanged){
                        sign.setLine(oldLine, text);
                        sign.update();
                        temp = 0;
                        hasLineChanged = false;
                        isTickerRunning = false;
                        this.cancel();
                    }
                }
            }.runTaskTimer(this.plugin, 20L, 20L);
        }
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
