package com.andrew121410.mc.world16.events;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignChangeEvent implements Listener {

    private World16Essentials plugin;
    private API api;

    public OnSignChangeEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void signChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("world16.sign.color") && api.isSignTranslateColors()) {
            for (int i = 0; i < 4; i++) {
                String line = event.getLine(i);
                if (line != null && !line.equals("")) event.setLine(i, Translate.chat(line));
            }
        }
    }
}
