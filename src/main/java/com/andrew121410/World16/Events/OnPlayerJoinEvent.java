package com.andrew121410.World16.Events;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.CustomConfigManager;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoinEvent implements Listener {

    private Main plugin;
    private CustomConfigManager customConfigManager;
    private API api;

    public OnPlayerJoinEvent(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        event.setJoinMessage("");

        if (api.getPlayersYML(customConfigManager, p).get("seats") == null) {
            api.getPlayersYML(customConfigManager, p).set("seats", true);
        }

        GameMode gameMode = null;
        String gamemode = this.api.getPlayersYML(this.customConfigManager, p).getString("Gamemode");
        try {
            if (gamemode != null) gameMode = GameMode.valueOf(gamemode);
        } catch (Exception ex) {
            p.sendMessage(Translate.chat("Error in OnPlayerJoinEvent send this text to Andrew121410#2035 on discord."));
        }
        if (p.hasPermission("world16.stay.creative") && gameMode == GameMode.CREATIVE) {
            p.setGameMode(GameMode.CREATIVE);
        }

        //Join message stuff.
        Bukkit.broadcastMessage(Translate.chat(API.PREFIX + " &6Welcome Back! " + p.getDisplayName()));
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
        p.sendMessage(Translate.chat("&4World1-6Ess Last Time Updated Was " + API.DATE_OF_VERSION));
        //...

        this.plugin.getPlayerInitializer().load(p);

        this.plugin.getDiscordBot().sendJoinMessage(p);
    }
}