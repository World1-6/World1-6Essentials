package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmspCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    public GmspCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("gmsp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.gmsp")) {
            api.permissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.setGameMode(GameMode.SPECTATOR);
            p.sendMessage(Translate.chat("&6Set game mode &cspectator&6 for " + ((Player) sender).getDisplayName()));
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.gmsp.other")) {
                api.permissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                target.setGameMode(GameMode.SPECTATOR);
                target.sendMessage(Translate.chat("&6Set game mode &cspectator&6 for " + target.getDisplayName()));
                p.sendMessage(Translate.chat("&6Set game mode &cspectator&6 for " + target.getDisplayName()));
            }
            return true;
        } else {
            p.sendMessage(Translate.chat("&aAliases: gmsp && gm3"));
            p.sendMessage(Translate.chat("&cUsage: for yourself do /gmsp OR /gm3 OR /gmsp <Player> OR /gm3 <Player>"));
        }
        return true;
    }
}