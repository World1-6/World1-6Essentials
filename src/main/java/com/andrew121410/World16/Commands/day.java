package com.andrew121410.World16.Commands;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class day implements CommandExecutor {

    private Main plugin;
    private API api;

    public day(Main getPlugin) {
        this.plugin = getPlugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("day").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.day")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        p.getLocation().getWorld().setTime(500);
        p.sendMessage(Translate.chat("&6The time was set to &eday&r."));
        return true;
    }
}