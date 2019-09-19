package World16.Events;

import World16.Main.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class OnPlayerInteractEvent implements Listener {

    //Maps
    private Map<String, Location> latestClickedBlocked;
    //...

    private Main plugin;

    public OnPlayerInteractEvent(Main plugin) {
        this.plugin = plugin;

        this.latestClickedBlocked = this.plugin.getSetListMap().getLatestClickedBlocked();

        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playeract(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        //Get's the latest clicked block and stores it in HashMap.
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            latestClickedBlocked.remove(p.getDisplayName()); //Removes old block
            latestClickedBlocked.put(p.getDisplayName(), event.getClickedBlock().getLocation());
        }
    }
}
