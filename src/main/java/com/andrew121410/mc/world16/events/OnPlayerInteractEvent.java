package com.andrew121410.mc.world16.events;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.objects.PowerToolObject;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.utils.UniverseBlockUtils;
import com.andrew121410.mc.world16utils.utils.xutils.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;
import java.util.UUID;

public class OnPlayerInteractEvent implements Listener {

    private Map<String, Location> latestClickedBlocked;
    private Map<UUID, PowerToolObject> powerToolMap;

    private Main plugin;
    private API api;

    public OnPlayerInteractEvent(Main plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.latestClickedBlocked = this.plugin.getSetListMap().getLatestClickedBlocked();
        this.powerToolMap = this.plugin.getSetListMap().getPowerToolMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block block = event.getClickedBlock();
        Action action = event.getAction();

        PowerToolObject powerToolObject = this.powerToolMap.get(p.getUniqueId());
        //Get's the latest clicked block and stores it in HashMap.
        if (action == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) {
            latestClickedBlocked.remove(p.getDisplayName()); //Removes old block
            latestClickedBlocked.put(p.getDisplayName(), block.getLocation());
            powerToolObject.runCommand(p, p.getInventory().getItemInMainHand().getType());
        } else if (action == Action.LEFT_CLICK_AIR && event.getHand() == EquipmentSlot.HAND) {
            powerToolObject.runCommand(p, p.getInventory().getItemInMainHand().getType());
        } else if (action == Action.PHYSICAL) {
            if (block != null) {
                if (UniverseBlockUtils.isFarmLand(XMaterial.matchXMaterial(block.getType())) && api.isPreventCropsTrampling()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}