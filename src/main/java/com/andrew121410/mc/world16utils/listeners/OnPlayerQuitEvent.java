package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16utils.World16Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    private World16Utils plugin;

    public OnPlayerQuitEvent(World16Utils plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.plugin.getChatClickCallbackManager().getMap().remove(player.getUniqueId());
    }
}
