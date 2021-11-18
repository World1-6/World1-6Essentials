package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LastJoinCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    public LastJoinCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("lastjoin").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.lastjoin")) {
            api.sendPermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: &6/lastjoin <Player>"));
            return true;
        } else if (args.length == 1) {
            UUID uuid = this.api.getUUIDFromMojangAPI(args[0]);
            OfflinePlayer offlinePlayer;

            if (uuid == null) {
                p.sendMessage(Translate.chat("Looks like that isn't a player."));
                return true;
            }

            Player playerTarget = this.plugin.getServer().getPlayer(uuid);
            if (playerTarget != null) offlinePlayer = playerTarget;
            else offlinePlayer = this.plugin.getServer().getOfflinePlayer(uuid);

            if (!offlinePlayer.hasPlayedBefore()) {
                p.sendMessage(Translate.chat("Looks like that player has never joined the server."));
                return true;
            }

            p.sendMessage(Translate.color("&6The last time " + offlinePlayer.getName() + " has played was " + api.getPlayerLastOnlineDateFormattedString(offlinePlayer)));
            return true;
        }
        return true;
    }
}
