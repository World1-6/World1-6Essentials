package com.andrew121410.mc.world16essentials.commands.playertime;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.CommandExecutor;

public class TimeOfLoginCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public TimeOfLoginCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.plugin.getCommand("timeoflogin").setExecutor(this);
    }

    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }

        if (!player.hasPermission("world16.timeoflogin")) {
            this.plugin.getApi().sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.color("&cUsage: /timeoflogin <player>"));
        } else if (args.length == 1) {
            org.bukkit.entity.Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Translate.color("&cPlayer not found."));
                return true;
            }
            player.sendMessage(Translate.color("&aTime of login of &6" + target.getName() + "&a is &6" + this.api.getTimeSinceLogin(target)));
        }
        return true;
    }
}
