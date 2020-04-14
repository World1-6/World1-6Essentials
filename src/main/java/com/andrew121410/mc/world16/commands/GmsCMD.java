package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmsCMD implements CommandExecutor {

    private Main plugin;
    private API api;

    public GmsCMD(Main plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("gms").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.gms")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.setGameMode(GameMode.SURVIVAL);
            p.sendMessage(Translate.chat("&6Set game mode &csurvival&6 for " + ((Player) sender).getDisplayName()));
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.gms.other")) {
                api.PermissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                target.setGameMode(GameMode.SURVIVAL);
                target.sendMessage(Translate.chat("&6Set game mode &csurvival&6 for " + target.getDisplayName()));
                p.sendMessage(Translate.chat("&6Set game mode &csurvival&6 for " + target.getDisplayName()));
            }
            return true;
        } else {
            p.sendMessage(Translate.chat("&aAliases: gms && gm0"));
            p.sendMessage(Translate.chat("&cUsage: for yourself do /gms OR /gm0 OR /gms <Player> OR /gm0 <Player>"));
        }
        return true;
    }
}