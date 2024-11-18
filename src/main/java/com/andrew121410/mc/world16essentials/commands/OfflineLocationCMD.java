package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.utils.TabUtils;
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

            UnlinkedWorldLocation location = new UnlinkedWorldLocation(target.getLocation());

            // Clickable message to teleport to the location.
            if (sender instanceof Player player) {
                sender.sendMessage(Translate.miniMessage("<yellow>Player: " + target.getName()));
                player.sendMessage(Translate.miniMessage("<yellow>World: " + location.getWorld().getName()));
                player.sendMessage(Translate.miniMessage("<yellow><u>Click me to teleport to the location!").clickEvent(this.plugin.getOtherPlugins().getWorld16Utils().getChatClickCallbackManager().create(player, (player1) -> {
                    if (!location.isWorldLoaded()) {
                        player.sendMessage(Translate.miniMessage("<red>Was unable to teleport to the location because the world isn't loaded."));
                        return;
                    }
                    player.teleportAsync(location);
                    player.sendMessage(Translate.miniMessage("<gold>You have been teleported to the location!"));
                })));
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
