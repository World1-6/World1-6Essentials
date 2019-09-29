package World16.Events;

import World16.Main.Main;
import World16FireAlarms.Screen.FireAlarmScreen;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class OnPlayerInteractEvent implements Listener {

    //Maps
    private Map<String, Location> latestClickedBlocked;
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;
    //...

    private Main plugin;

    public OnPlayerInteractEvent(Main plugin) {
        this.plugin = plugin;

        this.latestClickedBlocked = this.plugin.getSetListMap().getLatestClickedBlocked();
        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void playerinteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block block = event.getClickedBlock();

        Action action = event.getAction();

        //Get's the latest clicked block and stores it in HashMap.
        if (action == Action.RIGHT_CLICK_BLOCK) {
            latestClickedBlocked.remove(p.getDisplayName()); //Removes old block
            latestClickedBlocked.put(p.getDisplayName(), event.getClickedBlock().getLocation());
        }

        ItemStack itemStack = p.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (this.plugin.getApi().isFireAlarmsEnabled()) {
            if (block != null && action == Action.RIGHT_CLICK_BLOCK) {
                if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                    if (itemMeta != null && itemMeta.hasDisplayName()) {
                        FireAlarmScreen fireAlarmScreen = this.fireAlarmScreenMap.get(block.getLocation());
                        if (fireAlarmScreen != null && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("DOWN"))
                            this.fireAlarmScreenMap.get(block.getLocation()).changeLines();
                    } else {
                        FireAlarmScreen fireAlarmScreen = this.fireAlarmScreenMap.get(block.getLocation());
                        if (fireAlarmScreen != null) this.fireAlarmScreenMap.get(block.getLocation()).onClick(p);
                    }
                }
            }
        }
    }
}
