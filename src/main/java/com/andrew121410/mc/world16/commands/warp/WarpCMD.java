package com.andrew121410.mc.world16.commands.warp;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.tabcomplete.WarpTab;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class WarpCMD implements CommandExecutor {

    private Map<String, Location> warpsMap;

    private Main plugin;
    private API api;

    public WarpCMD(Main plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.warpsMap = this.plugin.getSetListMap().getWarpsMap();

        this.plugin.getCommand("warp").setExecutor(this);
        this.plugin.getCommand("warp").setTabCompleter(new WarpTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.warp")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: &6/warp <Name>"));
            return true;
        } else if (args.length == 1) {
            String name = args[0].toLowerCase();
            Location warp = this.warpsMap.get(name);

            if (warp == null) {
                p.sendMessage(Translate.chat("&cThat's not a warp."));
                return true;
            }

            p.teleport(warp);
            p.sendMessage(Translate.chat("&6Teleporting..."));
            return true;
        }
        return true;
    }
}