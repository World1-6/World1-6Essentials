package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class OnPlayerBedEnterEvent implements Listener {

    private final World16Essentials plugin;
    private boolean isSomeoneInBed = false;

    public OnPlayerBedEnterEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK && !this.isSomeoneInBed) {
            this.isSomeoneInBed = true;
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                player.getLocation().getWorld().setTime(0);
                Bukkit.broadcastMessage(Translate.chat("[&9World1-6&r]&6 Waky Waky Eggs And Baky&r."));
                isSomeoneInBed = false;
            }, 60L);
        }
    }
}
