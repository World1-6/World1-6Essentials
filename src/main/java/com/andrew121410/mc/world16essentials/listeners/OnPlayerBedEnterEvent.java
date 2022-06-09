package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

        if (this.plugin.getApi().getServerVersion().equals("1.12")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.getLocation().getWorld().setTime(0);
                    Bukkit.broadcastMessage(Translate.chat("[&9World1-6&r]&6 Waky Waky Eggs And Baky&r."));
                    isSomeoneInBed = false;
                }
            }.runTaskLater(this.plugin, 60L);
        } else {
            if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK && !this.isSomeoneInBed) {
                this.isSomeoneInBed = true;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.getLocation().getWorld().setTime(0);
                        Bukkit.broadcastMessage(Translate.chat("[&9World1-6&r]&6 Waky Waky Eggs And Baky&r."));
                        isSomeoneInBed = false;
                    }
                }.runTaskLater(this.plugin, 60L);
            }
        }
    }
}
