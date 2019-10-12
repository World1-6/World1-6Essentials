package World16.Events;

import World16.Main.Main;
import World16FireAlarms.Objects.Screen.FireAlarmScreen;
import World16FireAlarms.Objects.Screen.ScreenFocus;
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
import java.util.UUID;

public class OnPlayerInteractEvent implements Listener {

    //Maps
    private Map<String, Location> latestClickedBlocked;
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;
    private Map<UUID, ScreenFocus> screenFocusMap;
    //...

    private Main plugin;

    public OnPlayerInteractEvent(Main plugin) {
        this.plugin = plugin;

        this.latestClickedBlocked = this.plugin.getSetListMap().getLatestClickedBlocked();
        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();
        this.screenFocusMap = this.plugin.getSetListMap().getScreenFocusMap();

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
                if (block.getType() == Material.OAK_WALL_SIGN || block.getType() == Material.OAK_SIGN) {
                    if (this.screenFocusMap.get(p.getUniqueId()) == null && this.fireAlarmScreenMap.get(block.getLocation()) != null) {
                        this.screenFocusMap.putIfAbsent(p.getUniqueId(), new ScreenFocus(plugin, p));
                        return;
                    }
                    if (itemMeta != null && itemMeta.hasDisplayName()) {
                        FireAlarmScreen fireAlarmScreen = this.fireAlarmScreenMap.get(block.getLocation());
                        if (this.screenFocusMap.get(p.getUniqueId()) != null && itemMeta.getDisplayName().equalsIgnoreCase("Exit")) {
                            event.setCancelled(true);
                            this.screenFocusMap.get(p.getUniqueId()).revert();
                            this.screenFocusMap.remove(p.getUniqueId());
                        } else {
                            if (fireAlarmScreen != null) {
                                if (itemMeta.getDisplayName().equalsIgnoreCase("DOWN")) {
                                    this.fireAlarmScreenMap.get(block.getLocation()).changeLines(p);
                                } else if (itemMeta.getDisplayName().equalsIgnoreCase("SCROLL UP")) {
                                    this.fireAlarmScreenMap.get(block.getLocation()).onScroll(p, true);
                                } else if (itemMeta.getDisplayName().equalsIgnoreCase("SCROLL DOWN")) {
                                    this.fireAlarmScreenMap.get(block.getLocation()).onScroll(p, false);
                                }
                            }
                        }
                    } else {
                        FireAlarmScreen fireAlarmScreen = this.fireAlarmScreenMap.get(block.getLocation());
                        if (fireAlarmScreen != null) this.fireAlarmScreenMap.get(block.getLocation()).onClick(p);
                    }
                } else {
                    if (itemMeta != null && itemMeta.hasDisplayName()) {
                        if (this.screenFocusMap.get(p.getUniqueId()) != null && itemMeta.getDisplayName().equalsIgnoreCase("Exit")) {
                            event.setCancelled(true);
                            this.screenFocusMap.get(p.getUniqueId()).revert();
                            this.screenFocusMap.remove(p.getUniqueId());
                        }
                    }
                }
            }
        }
    }
}