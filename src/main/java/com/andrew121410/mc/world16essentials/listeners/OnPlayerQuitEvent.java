package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    private final World16Essentials plugin;
    private final API api;

    public OnPlayerQuitEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("");

        this.plugin.getPlayerInitializer().unload(player);
        Bukkit.broadcastMessage(api.parseMessage(player, api.getLeaveMessage()));
    }
}
