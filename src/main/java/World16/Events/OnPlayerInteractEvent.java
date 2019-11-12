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

        if (this.plugin.getApi().isFireAlarmsEnabled()) {
            if (block != null && action == Action.RIGHT_CLICK_BLOCK) {
                ItemStack itemStack = p.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();
                FireAlarmScreen fireAlarmScreen = this.fireAlarmScreenMap.get(block.getLocation());
                ScreenFocus screenFocus = this.screenFocusMap.get(p.getUniqueId());
                boolean isSign = false;

                if (block.getType() == Material.OAK_WALL_SIGN || block.getType() == Material.OAK_SIGN) isSign = true;

                if (!isSign && screenFocus != null) {
                    event.setCancelled(true);
                    screenFocus.revert();
                    this.screenFocusMap.remove(p.getUniqueId());
                }

                if (fireAlarmScreen == null) return;
                fireAlarmShit(event, p, itemMeta, fireAlarmScreen, screenFocus);
            }
        }
    }

    private void fireAlarmShit(PlayerInteractEvent event, Player p, ItemMeta itemMeta, FireAlarmScreen fireAlarmScreen, ScreenFocus screenFocus) {
        if (screenFocus == null) {
            fireAlarmScreen.tick(p);
            this.screenFocusMap.putIfAbsent(p.getUniqueId(), new ScreenFocus(plugin, p));
            return;
        }

        if (itemMeta != null && itemMeta.hasDisplayName()) {
            if (itemMeta.getDisplayName().equalsIgnoreCase("Exit")) {
                event.setCancelled(true);
                fireAlarmScreen.setStop(true);
                screenFocus.revert();
                this.screenFocusMap.remove(p.getUniqueId());
            } else if (itemMeta.getDisplayName().equalsIgnoreCase("DOWN")) {
                fireAlarmScreen.changeLines(p);
            } else if (itemMeta.getDisplayName().equalsIgnoreCase("SCROLL UP")) {
                fireAlarmScreen.onScroll(p, false);
            } else if (itemMeta.getDisplayName().equalsIgnoreCase("SCROLL DOWN")) {
                fireAlarmScreen.onScroll(p, true);
            }
        } else fireAlarmScreen.onClick(p);
    }
}