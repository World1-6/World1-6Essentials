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
        this.api = this.plugin.getApi();

        this.plugin.getCommand("ram").setExecutor(this);
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

        long maxMemory = (Runtime.getRuntime().maxMemory() / 1024 / 1024);
        long allocatedMemory = (Runtime.getRuntime().totalMemory() / 1024 / 1024);
        long freeMemory = (Runtime.getRuntime().freeMemory() / 1024 / 1024);
        long usedMemory = allocatedMemory - freeMemory;

        p.sendMessage(Translate.chat("&6Maximum memory: &c" + maxMemory + " MB."));
        p.sendMessage(Translate.chat("&6Allocated memory: &c" + allocatedMemory + " MB."));
        p.sendMessage(Translate.chat("&6Free memory: &c" + freeMemory + " MB."));
        p.sendMessage(Translate.chat("&6Used memory: &c " + usedMemory + " MB."));
        return true;
    }
}
