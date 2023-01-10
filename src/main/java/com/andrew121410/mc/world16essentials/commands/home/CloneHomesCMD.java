package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class CloneHomesCMD implements CommandExecutor {

    private final Map<UUID, Map<String, Location>> homesMap;

    private final World16Essentials plugin;
    private final API api;

    public CloneHomesCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.homesMap = this.plugin.getSetListMap().getHomesMap();

        this.plugin.getCommand("clonehomes").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.clonehomes")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.color("&cUsage: /clonehomes <player>"));
            return true;
        } else if (args.length == 1) {
            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[0]);
            if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                player.sendMessage(Translate.color("&cSeems like that player never existed!"));
                return true;
            }
            player.sendMessage(Translate.color("&6Please wait while we take all the homes from that player!"));
            this.plugin.getHomeManager().load(offlinePlayer); // Load OfflinePlayer into memory

            Map<String, Location> homesOfOther = this.homesMap.get(offlinePlayer.getUniqueId());
            // Have a delay, so it doesn't crash the server.
            new BukkitRunnable() {
                @Override
                public void run() {
                    Iterator<Map.Entry<String, Location>> iterator = homesOfOther.entrySet().iterator();
                    if (homesOfOther.isEmpty()) {
                        player.sendMessage(Translate.color("&6All of " + offlinePlayer.getName() + "'s homes have been cloned to you!"));
                        homesMap.remove(offlinePlayer.getUniqueId()); // Remove OfflinePlayer from memory.
                        this.cancel();
                        return;
                    }

                    while (iterator.hasNext()) {
                        Map.Entry<String, Location> pair = iterator.next();
                        String homeName = pair.getKey();
                        Location location = pair.getValue().clone();
                        plugin.getHomeManager().add(player, homeName, location);
                        player.sendMessage(Translate.color("&6You have cloned the home &e" + homeName));
                        iterator.remove();
                        break;
                    }
                }
            }.runTaskTimer(this.plugin, 20L, 10L);
        }
        return true;
    }
}