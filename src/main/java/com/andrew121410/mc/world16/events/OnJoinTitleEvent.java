package com.andrew121410.mc.world16.events;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.managers.TitleManager;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinTitleEvent implements Listener {

    private Main plugin;
    private FileConfiguration config;

    public OnJoinTitleEvent(Main plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        TitleManager.sendTitle(e.getPlayer(), 10, 5 * 20, 10, Translate.chat(config.getString("TittleTOP")),
                Translate.chat(config.getString("TittleBOTTOM")));
        TitleManager.sendTabTitle(e.getPlayer(), Translate.chat(config.getString("TablistTOP")),
                Translate.chat(config.getString("TablistBOTTOM")));
    }
}
