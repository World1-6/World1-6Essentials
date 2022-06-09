package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OnAsyncPlayerChatEvent implements Listener {

    private final World16Essentials plugin;
    private final API api;

    public OnAsyncPlayerChatEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void ChatEvent(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        //Name ping
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
            for (Player targetPlayer : plugin.getServer().getOnlinePlayers()) {
                if (message.contains(targetPlayer.getName())) {
                    if (api.isAfk(targetPlayer)) {
                        plugin.getServer().broadcastMessage(Translate.chat("&6[Afk] &eLooks like &r&9" + targetPlayer.getDisplayName() + "&r&e, is afk they may not respond."));
                    }
                    targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }

                if (message.contains("!" + targetPlayer.getDisplayName())) {
                    targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_HORSE_DEATH, 10.0f, 1.0f);
                }
            }
        });
    }
}