package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

public class LastJoinCMD implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public LastJoinCMD(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.customConfigManager = customConfigManager;
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
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: &6/lastjoin <Player>"));
            return true;
        } else if (args.length == 1) {
            UUID uuid = this.api.getUUIDFromMojangAPI(args[0]);

            if (uuid == null) {
                p.sendMessage(Translate.chat("Looks like that isn't a player."));
                return true;
            }

            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(uuid);

            if (!offlinePlayer.hasPlayedBefore()) {
                p.sendMessage(Translate.chat("Looks like that player has never joined the server."));
                return true;
            }

            LocalDate date = Instant.ofEpochMilli(offlinePlayer.getLastPlayed()).atZone(ZoneId.systemDefault()).toLocalDate();
            String formattedDate = date.getYear() + "-" + date.getMonth() + "-" + date.getDayOfMonth();
            p.sendMessage(Translate.color("&6The last time " + offlinePlayer.getName() + " has played was " + formattedDate));
            return true;
        }
        return true;
    }
}
