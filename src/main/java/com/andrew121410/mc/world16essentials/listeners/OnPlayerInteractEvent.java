package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.PowerToolObject;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.blocks.UniversalBlockUtils;
import com.andrew121410.mc.world16utils.utils.xutils.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.UUID;

public class OnPlayerInteractEvent implements Listener {

    private Map<String, Location> latestClickedBlocked;
    private Map<UUID, PowerToolObject> powerToolMap;

    private World16Essentials plugin;
    private API api;

    public OnPlayerInteractEvent(World16Essentials plugin) {
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
        if (action == Action.RIGHT_CLICK_BLOCK) {
            latestClickedBlocked.remove(p.getDisplayName()); //Removes old block
            latestClickedBlocked.put(p.getDisplayName(), block.getLocation());
            powerToolObject.runCommand(p, p.getInventory().getItemInMainHand().getType());
        } else if (action == Action.LEFT_CLICK_AIR) {
            powerToolObject.runCommand(p, p.getInventory().getItemInMainHand().getType());
        } else if (action == Action.PHYSICAL) {
            if (block != null) {
                if (UniversalBlockUtils.isFarmLand(XMaterial.matchXMaterial(block.getType())) && api.isPreventCropsTrampling()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}