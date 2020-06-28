package com.andrew121410.mc.world16.events;

import com.andrew121410.mc.world16.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;
import java.util.UUID;

public class OnPlayerDeathEvent implements Listener {

    private Main plugin;

    private Map<UUID, Map<String, Location>> backMap;

    public OnPlayerDeathEvent(Main plugin) {
        this.plugin = plugin;
        this.backMap = this.plugin.getSetListMap().getBackMap();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void OnDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Map<String, Location> playerBackMap = this.backMap.get(player.getUniqueId());
        if (playerBackMap != null) {
            playerBackMap.remove("Death");
            playerBackMap.put("Death", player.getLocation());
        }
    }
}