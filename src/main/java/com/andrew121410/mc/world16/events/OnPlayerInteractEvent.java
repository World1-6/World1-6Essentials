package com.andrew121410.mc.world16.events;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.objects.PowerToolObject;
import com.andrew121410.mc.world16.utils.API;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class OnPlayerInteractEvent implements Listener {

    private Map<String, Location> latestClickedBlocked;
    private Map<UUID, PowerToolObject> powerToolMap;
    private Map<Player, Arrow> sitMap;

    private Main plugin;
    private API api;
    private CustomConfigManager customConfigManager;

    private boolean isSitCheckerRunning = false;

    public OnPlayerInteractEvent(Main plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.customConfigManager = this.plugin.getCustomConfigManager();

        this.latestClickedBlocked = this.plugin.getSetListMap().getLatestClickedBlocked();
        this.powerToolMap = this.plugin.getSetListMap().getPowerToolMap();
        this.sitMap = this.plugin.getSetListMap().getSitMap();

        setupSeatChecker();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        PowerToolObject powerToolObject = this.powerToolMap.get(p.getUniqueId());
        //Get's the latest clicked block and stores it in HashMap.
        if (action == Action.RIGHT_CLICK_BLOCK) {
            latestClickedBlocked.remove(p.getDisplayName()); //Removes old block
            latestClickedBlocked.put(p.getDisplayName(), block.getLocation());

            //Seats
            if (block.getBlockData() instanceof Stairs && api.getPlayersYML(customConfigManager, p).getBoolean("seats")) {
                Stairs stairs = (Stairs) block.getBlockData();

                Block firstBlock = block.getRelative(BlockFace.UP);
                Block secondBlock = firstBlock.getRelative(BlockFace.UP);
                if (firstBlock.getType() == Material.AIR && secondBlock.getType() == Material.AIR) {
                    Arrow arrow = (Arrow) block.getWorld().spawnEntity(block.getLocation().add(0.5D, 0.2D, 0.5D), EntityType.ARROW);
                    arrow.addScoreboardTag("plugin-seat");
                    arrow.setGravity(false);
                    arrow.addPassenger(p);
                    sitMap.put(p, arrow);
                }
            }

            powerToolObject.runCommand(p, p.getInventory().getItemInMainHand().getType());
        } else if (action == Action.LEFT_CLICK_AIR) {
            powerToolObject.runCommand(p, p.getInventory().getItemInMainHand().getType());
        } else if (action == Action.PHYSICAL) {
            if (block != null) {
                if (block.getType() == Material.FARMLAND && api.isPreventCropsTrampling()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void setupSeatChecker() {
        //Don't run 2 times.
        if (isSitCheckerRunning) return;
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
            }
        }.runTaskTimer(plugin, 20 * 3, 20 * 3);
    }
}