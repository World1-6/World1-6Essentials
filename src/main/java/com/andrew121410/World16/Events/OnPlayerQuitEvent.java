package com.andrew121410.World16.Events;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.CustomConfigManager;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuitEvent implements Listener {

    private Main plugin;
    private CustomConfigManager customConfigManager;
    private API api;

    public OnPlayerQuitEvent(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onQUIT(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        //TEMP.yml
        ConfigurationSection configurationSection = this.api.getPlayersYML(customConfigManager, p);
        configurationSection.set("Gamemode", p.getGameMode().name());
        this.customConfigManager.getPlayersYml().saveConfig();
        //...

        this.plugin.getPlayerInitializer().unload(p);

        event.setQuitMessage("");
        Bukkit.broadcastMessage(Translate.chat(API.PREFIX + " &5Bye Bye, " + p.getDisplayName()));

        this.plugin.getDiscordBot().sendLeaveMessage(p);
    }
}
