package com.andrew121410.mc.world16essentials.commands.playertime;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class FirstJoinedCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public FirstJoinedCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.plugin.getCommand("firstjoined").setExecutor(this);
    }

    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("world16.firstjoined")) {
            this.plugin.getApi().sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.color("&cUsage: /firstjoined <player>"));
        } else if (args.length == 1) {
            OfflinePlayer target = this.plugin.getServer().getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore()) {
                player.sendMessage(Translate.color("&cPlayer not found."));
                return true;
            }
            player.sendMessage(Translate.color("&aFirst joined of &6" + target.getName() + "&a is &6" + this.api.getTimeSinceFirstLogin(target)));
        }
        return true;
    }
}