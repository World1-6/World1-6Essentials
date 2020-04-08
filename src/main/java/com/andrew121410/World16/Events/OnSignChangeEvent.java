package com.andrew121410.World16.Events;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignChangeEvent implements Listener {

    private Main plugin;
    private API api;

    public OnSignChangeEvent(Main plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void signChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("com.andrew121410.World16.sign.color") && api.isSignTranslateColors()) {
            for (int i = 0; i < 4; i++) {
                String line = event.getLine(i);
                if (line != null && !line.equals("")) event.setLine(i, Translate.chat(line));
            }
        }
    }
}
