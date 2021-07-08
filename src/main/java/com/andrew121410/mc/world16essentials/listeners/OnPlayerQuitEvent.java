package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    private World16Essentials plugin;

    public OnPlayerQuitEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onQUIT(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        this.plugin.getPlayerInitializer().unload(p);
        event.setQuitMessage("");
        Bukkit.broadcastMessage(Translate.chat(API.PREFIX + " &5Bye Bye, " + p.getDisplayName()));
    }
}
