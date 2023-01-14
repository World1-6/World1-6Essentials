package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.sharedtabcomplete.HomeListTab;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class HomeCMD implements CommandExecutor {

    private final Map<UUID, Map<String, Location>> homesMap;

    private final World16Essentials plugin;
    private final API api;

    public HomeCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.homesMap = this.plugin.getSetListMap().getHomesMap();

        this.plugin.getCommand("home").setExecutor(this);
        this.plugin.getCommand("home").setTabCompleter(new HomeListTab(this.plugin));
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.home")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        Map<String, Location> homes = this.homesMap.get(player.getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage(Translate.colorc("&cYou don't have any homes."));
            return true;
        }

        String homeName = "home";
        if (args.length == 1) {
            homeName = args[0].toLowerCase();
        }

        Location home = homes.get(homeName);
        if (home != null) {
            player.teleport(home);
            player.sendMessage(Translate.color("&6Teleporting..."));
        } else {
            player.sendMessage(Translate.color("&cHome Not Found."));
        }
        return true;
    }
}