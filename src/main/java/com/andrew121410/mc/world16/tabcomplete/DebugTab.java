package com.andrew121410.mc.world16.tabcomplete;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.utils.Software;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DebugTab implements TabCompleter {

    private Map<String, List<String>> tabCompleteMap;

    private World16Essentials plugin;

    public DebugTab(World16Essentials plugin) {
        this.plugin = plugin;
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();

        tabCompleteMap.computeIfAbsent("debug1-6", k -> new ArrayList<>());
        if (tabCompleteMap.get("debug1-6").isEmpty()) {
            tabCompleteMap.get("debug1-6").add("date");
            tabCompleteMap.get("debug1-6").add("reload");
            tabCompleteMap.get("debug1-6").add("load");
            tabCompleteMap.get("debug1-6").add("unload");
            tabCompleteMap.get("debug1-6").add("convert");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("debug1-6") || !p.hasPermission("world16.debug")) {
            return null;
        }

        if (args.length == 1) {
            return TabUtils.getContainsString(args[0], tabCompleteMap.get("debug1-6"));
        } else if (args[0].equalsIgnoreCase("convert")) {
            if (args.length == 2) {
                List<String> typesOfSoftwareList = Arrays.asList(Software.ESSENTIALS_X.name(), Software.ANDREWS_ESSENTIALS_FABRIC_MOD.name());
                return TabUtils.getContainsString(args[1], typesOfSoftwareList);
            } else if (args.length == 3) {
                return TabUtils.getContainsString(args[2], Arrays.asList("homes"));
            }
        }
        return null;
    }
}