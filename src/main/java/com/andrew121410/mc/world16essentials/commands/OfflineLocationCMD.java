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

            if (args.length == 1 && this.plugin.getApi().getConfigUtils().isOfflinePlayersTabCompletion()) {
                return TabUtils.getContainsString(args[0], TabUtils.getOfflinePlayerNames(this.plugin.getServer().getOfflinePlayers()));
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
