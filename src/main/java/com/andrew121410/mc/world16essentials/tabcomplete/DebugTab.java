package com.andrew121410.mc.world16essentials.tabcomplete;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.datatranslator.Software;
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

    private final Map<String, List<String>> tabCompleteMap;

    private final World16Essentials plugin;

    public DebugTab(World16Essentials plugin) {
        this.plugin = plugin;
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();

        tabCompleteMap.computeIfAbsent("debug1-6", k -> new ArrayList<>());
        if (tabCompleteMap.get("debug1-6").isEmpty()) {
            tabCompleteMap.get("debug1-6").add("reload");
            tabCompleteMap.get("debug1-6").add("load");
            tabCompleteMap.get("debug1-6").add("unload");
            tabCompleteMap.get("debug1-6").add("convert");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player player)) {
            return null;
        }

        if (!player.hasPermission("world16.debug")) return null;

        if (args.length == 1) {
            return TabUtils.getContainsString(args[0], tabCompleteMap.get("debug1-6"));
        } else if (args[0].equalsIgnoreCase("convert")) {
            if (args.length == 2) {
                return TabUtils.getContainsString(args[1], Arrays.asList("from", "to"));
            } else if (args.length == 3) {
                List<String> typesOfSoftwareList = Arrays.stream(Software.values()).map(Enum::name).toList();
                return TabUtils.getContainsString(args[2], typesOfSoftwareList);
            }
        }
        return null;
    }
}