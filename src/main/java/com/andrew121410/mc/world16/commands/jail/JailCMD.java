package com.andrew121410.mc.world16.commands.jail;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.managers.JailManager;
import com.andrew121410.mc.world16.tabcomplete.JailTab;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class JailCMD implements CommandExecutor {

    //Maps
    private Map<String, Location> jailsMap;
    //...

    private Main plugin;
    private API api;
    private JailManager jailManager;

    public JailCMD(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.jailsMap = this.plugin.getSetListMap().getJails();

        this.api = new API(this.plugin);
        this.jailManager = this.plugin.getJailManager();

        this.plugin.getCommand("jail").setExecutor(this);
        this.plugin.getCommand("jail").setTabCompleter(new JailTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.jail")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: &6/jail <Name>"));
            return true;
        } else if (args.length == 1) {
            String jailName = args[0].toLowerCase();

            if (jailsMap.get(jailName) == null) {
                p.sendMessage(Translate.chat("That's not a jail."));
                return true;
            }

            p.teleport(jailsMap.get(jailName));
            p.sendMessage(Translate.chat("&6Teleporting..."));
            return true;
        }
        return true;
    }
}
