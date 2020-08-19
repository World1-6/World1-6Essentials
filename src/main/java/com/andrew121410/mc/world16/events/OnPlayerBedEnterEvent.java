package com.andrew121410.mc.world16.events;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class OnPlayerBedEnterEvent implements Listener {

    private Main plugin;

    public OnPlayerBedEnterEvent(Main plugin) {
        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.getLocation().getWorld().setTime(0);
                    Bukkit.broadcastMessage(Translate.chat("[&9World1-6&r]&6 Waky Waky Eggs And Baky&r."));
                }
            }.runTaskLater(this.plugin, 60L);
        }
    }
}
