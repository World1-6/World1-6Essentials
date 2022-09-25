package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.BukkitSerialization;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoinEvent implements Listener {

    private final World16Essentials plugin;
    private final API api;

    public OnPlayerJoinEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");

        // Kit
        if (!player.hasPlayedBefore()) {
            for (KitObject value : this.plugin.getSetListMap().getKitsMap().values()) {
                if (value.getSettings().isGiveOnFirstJoin()) {
                    this.plugin.getKitSettingsManager().setLastUsed(player, value);
                    BukkitSerialization.giveFromBase64s(player, value.getData());
                }
            }
        }

        String message = player.hasPlayedBefore() ? this.api.getMessagesUtils().getWelcomeBackMessage() : this.api.getMessagesUtils().getFirstJoinedMessage();
        Bukkit.broadcastMessage(api.parseMessage(player, message));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);

        if (player.isOp()) {
            player.sendMessage(Translate.color("&cWorld1-6Essentials was last updated on " + API.DATE_OF_VERSION));
        }

        this.plugin.getPlayerInitializer().load(player);
    }
}