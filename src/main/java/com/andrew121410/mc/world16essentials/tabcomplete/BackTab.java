package com.andrew121410.mc.world16essentials.tabcomplete;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BackTab implements TabCompleter {

    //Maps
    private Map<String, List<String>> tabCompleteMap;
    //...

    private World16Essentials plugin;

    public BackTab(World16Essentials plugin) {
        this.plugin = plugin;
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();

        tabCompleteMap.computeIfAbsent("back", k -> new ArrayList<>());

        if (tabCompleteMap.get("back").isEmpty()) {
            tabCompleteMap.get("back").add("death");
            tabCompleteMap.get("back").add("tp");
//            tabCompleteMap.get("back").add("");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("back") || !p.hasPermission("world16.back")) {
            return null;
        }

        if (args.length == 1) {
            return TabUtils.getContainsString(args[0], tabCompleteMap.get("back"));
        }

        return null;
    }
}