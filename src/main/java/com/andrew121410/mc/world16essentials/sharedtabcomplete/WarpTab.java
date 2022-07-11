package com.andrew121410.mc.world16essentials.sharedtabcomplete;

import com.andrew121410.mc.world16essentials.World16Essentials;
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

    private final Map<String, Location> warpsMap;

    private final World16Essentials plugin;

    public WarpTab(World16Essentials plugin) {
        this.plugin = plugin;
        this.warpsMap = this.plugin.getSetListMap().getWarpsMap();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) return null;
        
        Player player = (Player) sender;

        if (!player.hasPermission("world16.warp")) return null;

        List<String> keys = new ArrayList<>(this.warpsMap.keySet());
        if (args.length == 1) return TabUtils.getContainsString(args[0], keys);
        return null;
    }
}
