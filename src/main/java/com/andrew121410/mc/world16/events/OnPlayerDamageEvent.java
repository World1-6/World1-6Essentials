package com.andrew121410.mc.world16.events;

import com.andrew121410.mc.world16.World16Essentials;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class OnPlayerDamageEvent implements Listener {

    private World16Essentials plugin;

    private List<String> godList;

    public OnPlayerDamageEvent(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.godList = this.plugin.getSetListMap().getGodList();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getEntity();

        if (godList.contains(p.getDisplayName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageBy(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) event.getEntity();

        if (godList.contains(p.getDisplayName())) {
            event.setCancelled(true);
        }
    }
}
