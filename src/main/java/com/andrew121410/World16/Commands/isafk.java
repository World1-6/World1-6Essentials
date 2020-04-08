package com.andrew121410.World16.Commands;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.CustomConfigManager;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class isafk implements CommandExecutor {

    private Main plugin;
    private API api;

    public isafk(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("isafk").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.isafk")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 1) {
            Player playerFromArg = this.plugin.getServer().getPlayerExact(args[0]);

            if (playerFromArg == null) {
                p.sendMessage(Translate.chat("&9[AfkChecker]&r&c I don't think that's a player."));
                return true;
            }

            if (api.isAfk(playerFromArg)) {
                p.sendMessage(Translate.chat("&aThe Player: " + playerFromArg.getDisplayName() + " is afk"));
                return true;
            } else {
                p.sendMessage(Translate.chat("&cThe Player: " + playerFromArg.getDisplayName() + " is not afk!"));
                return true;
            }
        } else {
            p.sendMessage(Translate.chat("&e-----&9[AfkChecker]&r&e-----&r"));
            p.sendMessage(Translate.chat("&6/isafk <User> &9[Check's if user is AFK or not.]"));
        }
        return true;
    }
}