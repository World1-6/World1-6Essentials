package com.andrew121410.mc.world16utils.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.World16Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    private World16Utils plugin;
    private World16Essentials plugin1;

    public OnPlayerQuitEvent(World16Utils plugin, World16Essentials plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
        this.plugin1.getServer().getPluginManager().registerEvents(this, this.plugin1);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
//        this.plugin.getChatResponseManager().getMap().remove(player.getUniqueId());
    }
}
