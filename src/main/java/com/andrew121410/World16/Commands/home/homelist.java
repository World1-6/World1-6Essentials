package com.andrew121410.World16.Commands.home;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class homelist implements CommandExecutor {

    private Map<UUID, Map<String, Location>> homesMap;

    private Main plugin;
    private API api;

    public homelist(Main plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.homesMap = this.plugin.getSetListMap().getHomesMap();

        this.plugin.getCommand("homelist").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.home")) {
            api.PermissionErrorMessage(p);
            return true;
        }
        Set<String> homeSet = homesMap.get(p.getUniqueId()).keySet();
        String[] homeString = homeSet.toArray(new String[0]);
        Arrays.sort(homeString);
        String str = String.join(", ", homeString);
        String homeListPrefix = "&6Homes:&r&7";

        p.sendMessage(Translate.chat(homeListPrefix + " " + str));
        return true;
    }
}