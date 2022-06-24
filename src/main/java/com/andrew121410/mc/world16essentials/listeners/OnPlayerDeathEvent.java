package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.commands.back.BackEnum;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;
import java.util.UUID;

public class OnPlayerDeathEvent implements Listener {

    private final World16Essentials plugin;

    private final Map<UUID, Map<BackEnum, Location>> backMap;

    public OnPlayerDeathEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.backMap = this.plugin.getSetListMap().getBackMap();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void OnDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        Map<BackEnum, Location> playerBackMap = this.backMap.get(player.getUniqueId());
        if (playerBackMap != null) {
            playerBackMap.remove(BackEnum.DEATH);
            playerBackMap.put(BackEnum.DEATH, player.getLocation());
        }
    }
}