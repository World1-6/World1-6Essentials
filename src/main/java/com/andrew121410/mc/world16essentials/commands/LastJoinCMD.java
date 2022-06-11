package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LastJoinCMD implements CommandExecutor {

    private final World16Essentials plugin;

    public LastJoinCMD(World16Essentials plugin) {
        this.plugin = plugin;

        this.plugin.getCommand("lastjoin").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.lastjoin")) {
            this.plugin.getApi().sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.chat("&cUsage: &6/lastjoin <Player>"));
            return true;
        } else if (args.length == 1) {
            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[0]);

            if (!offlinePlayer.hasPlayedBefore()) {
                player.sendMessage(Translate.chat("&cThis user has never joined before..."));
                return true;
            }

            player.sendMessage(Translate.chat("&aLast join of &6" + offlinePlayer.getName() + "&a was &6" + this.plugin.getApi().getTimeSinceLastLogin(offlinePlayer)));
        }
        return true;
    }
}
