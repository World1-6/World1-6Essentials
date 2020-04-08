package com.andrew121410.World16.Commands;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ram implements CommandExecutor {

    private Main plugin;
    private API api;

    public ram(Main plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);
        plugin.getCommand("ram").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.ram")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        p.sendMessage(Translate.chat("&6Maximum memory: &c" + (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " MB."));
        p.sendMessage(Translate.chat("&6Allocated memory: &c" + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + " MB."));
        p.sendMessage(Translate.chat("&6Free memory: &c" + (Runtime.getRuntime().freeMemory() / 1024 / 1024) + " MB."));
        return true;
    }
}
