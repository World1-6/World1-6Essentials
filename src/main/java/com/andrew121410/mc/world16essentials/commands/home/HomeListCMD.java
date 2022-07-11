package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HomeListCMD implements CommandExecutor {

    private final Map<UUID, Map<String, Location>> homesMap;

    private final World16Essentials plugin;
    private final API api;

    public HomeListCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.homesMap = this.plugin.getSetListMap().getHomesMap();

        this.plugin.getCommand("homelist").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("world16.home")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }
        Set<String> homeSet = homesMap.get(player.getUniqueId()).keySet();
        String[] homeString = homeSet.toArray(new String[0]);
        Arrays.sort(homeString);
        String str = String.join(", ", homeString);
        String homeListPrefix = "&6Homes:&r&7";

        player.sendMessage(Translate.chat(homeListPrefix + " " + str));
        return true;
    }
}