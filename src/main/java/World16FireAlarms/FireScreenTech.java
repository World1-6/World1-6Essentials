package World16FireAlarms;

import World16.Main.Main;
import org.bukkit.block.Sign;

public class FireScreenTech {

    private Main plugin;

    public FireScreenTech(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onLine(Sign sign, int lineNumber) {
        return true;
    }
}
