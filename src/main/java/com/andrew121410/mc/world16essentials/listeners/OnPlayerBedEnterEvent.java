package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class OnPlayerBedEnterEvent implements Listener {

    private final World16Essentials plugin;
    private final API api;
    private boolean isSomeoneInBed = false;

    public OnPlayerBedEnterEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();

        this.isSomeoneInBed = true;
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
            player.getLocation().getWorld().setTime(0);
            Bukkit.broadcastMessage(api.parseMessage(player, api.getMessagesUtils().getBedMessage()));
            isSomeoneInBed = false;
        }, 60L);
    }
}
