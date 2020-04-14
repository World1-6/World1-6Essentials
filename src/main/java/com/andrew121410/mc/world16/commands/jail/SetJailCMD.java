package com.andrew121410.mc.world16.commands.jail;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.managers.JailManager;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class SetJailCMD implements CommandExecutor {

    //Maps
    private Map<String, Location> jailsMap;
    //...

    private Main plugin;

    private JailManager jailManager;
    private API api;

    public SetJailCMD(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.jailsMap = this.plugin.getSetListMap().getJails();

        this.jailManager = this.plugin.getJailManager();
        this.api = new API(this.plugin);

        this.plugin.getCommand("setjail").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.setjail")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&2[SetJail]&r&c Usage: /setjail <JailName>"));
            return true;
        } else if (args.length == 1) {
            this.jailsMap.putIfAbsent(args[0].toLowerCase(), p.getLocation());
            p.sendMessage(Translate.chat("&2[SetJail]&r&6 The Jail has been set."));
            return true;
        }
        return true;
    }
}
