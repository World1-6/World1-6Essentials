package com.andrew121410.mc.world16.tabcomplete;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarpTab implements TabCompleter {

    //Maps
    private Map<String, Location> warpsMap;
    //...

    private World16Essentials plugin;

    public WarpTab(World16Essentials plugin) {
        this.plugin = plugin;

        this.warpsMap = this.plugin.getSetListMap().getWarpsMap();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.warp")) {
            return null;
        }

        List<String> keys = new ArrayList<>(this.warpsMap.keySet());

        if (args.length == 1) {
            return TabUtils.getContainsString(args[0], keys);
        }

        return null;
    }
}
