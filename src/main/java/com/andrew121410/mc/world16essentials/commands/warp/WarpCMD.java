package com.andrew121410.mc.world16essentials.commands.warp;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.sharedtabcomplete.WarpTab;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class WarpCMD implements CommandExecutor {

    private final Map<String, Location> warpsMap;

    private final World16Essentials plugin;
    private final API api;

    public WarpCMD(World16Essentials plugin) {
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
            api.sendPermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.color("&cUsage: &6/warp <Name>"));
            return true;
        } else if (args.length == 1) {
            String name = args[0].toLowerCase();
            Location warp = this.warpsMap.get(name);

            if (warp == null) {
                p.sendMessage(Translate.color("&cThat's not a warp."));
                return true;
            }

            p.teleport(warp);
            p.sendMessage(Translate.color("&6Teleporting..."));
            return true;
        }
        return true;
    }
}