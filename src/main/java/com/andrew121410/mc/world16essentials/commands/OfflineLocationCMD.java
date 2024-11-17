package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OfflineLocationCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public OfflineLocationCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("offlinelocation").setExecutor(this);
        this.plugin.getCommand("offlinelocation").setTabCompleter((sender, cmd, alias, args) -> {
            if (!(sender instanceof Player player)) return null;
            if (!player.hasPermission("world16.offlinelocation")) return null;

            if (args.length == 1) {
                // Get the array of OfflinePlayers
                OfflinePlayer[] playersArray = this.plugin.getServer().getOfflinePlayers();

                // Filter out broken players and collect names into a list
                List<String> offlineNames = Arrays.stream(playersArray)
                        .filter(Objects::nonNull) // Filter out null OfflinePlayers
                        .filter(offlinePlayer -> offlinePlayer.getName() != null) // Filter out players with null names
                        .filter(offlinePlayer -> !offlinePlayer.getName().isEmpty()) // Filter out players with empty names
                        .filter(offlinePlayer -> !offlinePlayer.getName().equals("null")) // Filter out players with "null" as their name
                        .map(OfflinePlayer::getName) // Map to player names
                        .collect(Collectors.toList()); // Collect names into a list

                return TabUtils.getContainsString(args[0], offlineNames);
            }

            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.offlinelocation")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length == 1) {
            OfflinePlayer target = this.plugin.getServer().getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage(Translate.miniMessage("<red>Player has never played before!"));
                return true;
            }

            if (target.isOnline()) {
                sender.sendMessage(Translate.miniMessage("<green>That player is online."));
                target = target.getPlayer();
            }

            Location location = target.getLocation();
            if (location == null) {
                sender.sendMessage(Translate.miniMessage("<red>Player has never played before!"));
                return true;
            }

            // Clickable message to teleport to the location.
            if (sender instanceof Player player) {
                sender.sendMessage(Translate.miniMessage("<yellow>Player: " + target.getName()));
                player.sendMessage(Translate.miniMessage("<yellow>World: " + location.getWorld().getName()));
                player.sendMessage(Translate.miniMessage("<yellow><u>Click me to teleport to the location!").clickEvent(ClickEvent.runCommand("/tp " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ())));
            } else {
                sender.sendMessage("Player: " + target.getName());
                sender.sendMessage("World: " + location.getWorld().getName());
                sender.sendMessage("There location is " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
            }
            return true;
        } else {
            sender.sendMessage(Translate.color("&cUsage: /offlinelocation <player>"));
        }
        return true;
    }
}
