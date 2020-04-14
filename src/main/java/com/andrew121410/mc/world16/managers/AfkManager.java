package com.andrew121410.mc.world16.managers;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.objects.AfkObject;
import com.andrew121410.mc.world16.utils.API;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class AfkManager {

    private Map<UUID, AfkObject> afkMap;

    private Main plugin;
    private API api;

    public AfkManager(Main plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.afkMap = this.plugin.getSetListMap().getAfkMap();

        afkChecker();
    }

    private void afkChecker() {
//        Checks if the player is afk. 3m
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, AfkObject> entry : afkMap.entrySet()) {
                    Player player = plugin.getServer().getPlayer(entry.getKey());
                    String color = "&7";

                    //Checks if player is op if so then change the color to red.
                    if (player.isOp()) {
                        color = "&4";
                    }

                    //Don't run if player is already AFK.
                    if (entry.getValue().isAfk()) return;

                    //Checks if the player has not moved in 3 min if not afk them if so restart().
                    if (player.getLocation().equals(entry.getValue().getLocation())) {
                        api.doAfk(player, color);
                    } else entry.getValue().restart(player);
                }
            }
        }.runTaskTimer(plugin, 1200L, 2400L);

        //Checks if the player moves.
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, AfkObject> entry : afkMap.entrySet()) {
                    UUID uuid = entry.getKey();
                    AfkObject afkObject = entry.getValue();
                    Player player = plugin.getServer().getPlayer(uuid);
                    String color = "&7";

                    //Checks if player is op if so then change the color to red.
                    if (player.isOp()) {
                        color = "&4";
                    }

                    //If not afk don't run.
                    if (!afkObject.isAfk()) return;

                    //Only if the player moves more then 3 blocks.
                    if (player.getLocation().distanceSquared(afkObject.getLocation()) > 9) {
                        api.doAfk(player, color);
                    }
                }
            }
        }.runTaskTimer(this.plugin, 1200L, 40L);
    }
}
