package com.andrew121410.World16.Events;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.TitleManager;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinTitleEvent implements Listener {

    private Main plugin;
    private FileConfiguration file;

    public OnJoinTitleEvent(Main plugin) {
        this.plugin = plugin;
        this.file = this.plugin.getConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        TitleManager.sendTitle(e.getPlayer(), 10, 5 * 20, 10, Translate.chat(file.getString("TittleTOP")),
                Translate.chat(file.getString("TittleBOTTOM")));
        TitleManager.sendTabTitle(e.getPlayer(), Translate.chat(file.getString("TablistTOP")),
                Translate.chat(file.getString("TablistBOTTOM")));
    }
}
