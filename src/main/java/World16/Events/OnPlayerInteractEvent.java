package World16.Events;

import World16.Main.Main;
import World16.Objects.PowerToolObject;
import World16.Utils.SignUtils;
import World16FireAlarms.Objects.Screen.FireAlarmScreen;
import World16FireAlarms.Objects.Screen.ScreenFocus;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class OnPlayerInteractEvent implements Listener {

    //Maps
    private Map<String, Location> latestClickedBlocked;
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;
    private Map<UUID, ScreenFocus> screenFocusMap;
    private Map<UUID, PowerToolObject> powerToolMap;
    private Map<Player, Arrow> sitMap;
    //...

    private Main plugin;

    public OnPlayerInteractEvent(Main plugin) {
        this.plugin = plugin;

        this.latestClickedBlocked = this.plugin.getSetListMap().getLatestClickedBlocked();
        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();
        this.screenFocusMap = this.plugin.getSetListMap().getScreenFocusMap();
        this.powerToolMap = this.plugin.getSetListMap().getPowerToolMap();
        this.sitMap = this.plugin.getSetListMap().getSitMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void playerinteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        PowerToolObject powerToolObject = this.powerToolMap.get(p.getUniqueId());
        //Get's the latest clicked block and stores it in HashMap.
        if (action == Action.RIGHT_CLICK_BLOCK) {
            latestClickedBlocked.remove(p.getDisplayName()); //Removes old block
            latestClickedBlocked.put(p.getDisplayName(), event.getClickedBlock().getLocation());

            //Stairs
            if (block.getBlockData() instanceof Stairs) {
                Stairs stairs = (Stairs) block.getBlockData();

                Arrow arrow = (Arrow) block.getWorld().spawnEntity(block.getLocation().add(0.5D, 0.2D, 0.5D), EntityType.ARROW);
                arrow.setGravity(false);
                arrow.addPassenger(p);
                sitMap.putIfAbsent(p, arrow);
                setupSitChecker();
            }

            powerToolObject.runCommand(p, p.getInventory().getItemInMainHand().getType());
        } else if (action == Action.LEFT_CLICK_AIR) {
            powerToolObject.runCommand(p, p.getInventory().getItemInMainHand().getType());
        }

        if (this.plugin.getApi().isFireAlarmsEnabled()) {
            if (block != null && action == Action.RIGHT_CLICK_BLOCK) {
                ItemStack itemStack = p.getInventory().getItemInMainHand();
                ItemMeta itemMeta = itemStack.getItemMeta();
                FireAlarmScreen fireAlarmScreen = this.fireAlarmScreenMap.get(block.getLocation());
                ScreenFocus screenFocus = this.screenFocusMap.get(p.getUniqueId());
                boolean isSign = false;

                if (SignUtils.isSign(block) != null) isSign = true;

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

    private boolean isSitCheckerRunning = false;

    private void setupSitChecker() {
        //DONT RUN TWO TIMES.
        if (isSitCheckerRunning) {
            return;
        }
        isSitCheckerRunning = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Map.Entry<Player, Arrow>> iterator = sitMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Player, Arrow> entry = iterator.next();
                    if (!entry.getKey().isInsideVehicle()) {
                        entry.getValue().remove();
                        iterator.remove();
                    }
                }
                isSitCheckerRunning = false;
                this.cancel();
            }
        }.runTaskTimer(plugin, 20 * 2, 20 * 2);
    }
}