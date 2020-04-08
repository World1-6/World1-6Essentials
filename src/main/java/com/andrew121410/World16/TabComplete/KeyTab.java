package com.andrew121410.World16.TabComplete;

import com.andrew121410.World16.Main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyTab implements TabCompleter {

    //Maps
    private Map<String, List<String>> tabCompleteMap;
    //...

    //Lists
    //...

    private Main plugin;

    public KeyTab(Main plugin) {
        this.plugin = plugin;
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();

        tabCompleteMap.computeIfAbsent("key", k -> new ArrayList<>());

        if (tabCompleteMap.get("key").isEmpty()) {
            tabCompleteMap.get("key").add("set");
            tabCompleteMap.get("key").add("give");
            tabCompleteMap.get("key").add("reset");
            tabCompleteMap.get("key").add("list");
//            tabCompleteMap.get("key").add("");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("key") || !p.hasPermission("world16.key")) {
            return null;
        }

        if (args.length == 1) {
            return TabUtils.getContainsString(args[0], tabCompleteMap.get("key"));
        }

        return null;
    }
}