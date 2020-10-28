package com.andrew121410.mc.world16.tabcomplete;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class HomeListTab implements TabCompleter {

    //This is just a reminder for myself to not copy this because it won't work with other stuff since it's custom coded.

    //Lists
    private Map<UUID, Map<String, Location>> rawHomesMap;
    //...

    private World16Essentials plugin;

    public HomeListTab(World16Essentials plugin) {
        this.plugin = plugin;

        this.rawHomesMap = this.plugin.getSetListMap().getHomesMap();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.home")) {
            return null;
        }

        if (args.length == 1) {
            if (rawHomesMap.get(p.getUniqueId()) == null) {
                p.kickPlayer("[HomeTabComplete] You where not in the memory so NPE was caused.");
                return null;
            }
            Set<String> homeSet = rawHomesMap.get(p.getUniqueId()).keySet();
            String[] homeString = homeSet.toArray(new String[0]);
            return TabUtils.getContainsString(args[0], Arrays.asList(homeString));
        }

        return null;
    }
}