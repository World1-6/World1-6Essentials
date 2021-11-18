package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmaCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    public GmaCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);
        plugin.getCommand("gma").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.gma")) {
            api.sendPermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.setGameMode(GameMode.ADVENTURE);
            p.sendMessage(Translate.chat("&6Set game mode &cadventure&6 for " + ((Player) sender).getDisplayName()));
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.gma.other")) {
                api.sendPermissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                target.setGameMode(GameMode.ADVENTURE);
                target.sendMessage(Translate.chat("&6Set game mode &cadventure&6 for " + target.getDisplayName()));
                p.sendMessage(Translate.chat("&6Set game mode &cadventure&6 for " + target.getDisplayName()));
            }
            return true;
        } else {
            p.sendMessage(Translate.chat("&aAliases: gma && gm2"));
            p.sendMessage(Translate.chat("&cUsage: for yourself do /gma OR /gm2 OR /gma <Player> OR /gm2 <Player>"));
        }
        return true;
    }
}