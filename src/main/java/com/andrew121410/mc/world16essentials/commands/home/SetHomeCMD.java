package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class SetHomeCMD implements CommandExecutor {

    private final Map<UUID, Map<String, Location>> homesMap;

    private final World16Essentials plugin;
    private final API api;

    public SetHomeCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.homesMap = this.plugin.getSetListMap().getHomesMap();

        this.plugin.getCommand("sethome").setExecutor(this);
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
        String defaultHomeName = "home";

        if (args.length == 1) {
            defaultHomeName = args[0].toLowerCase();
        }

        this.plugin.getHomeManager().save(player.getUniqueId(), player.getDisplayName(), defaultHomeName, player.getLocation());
        this.homesMap.get(player.getUniqueId()).put(defaultHomeName.toLowerCase(), player.getLocation());
        player.sendMessage(Translate.chat("&9[Homes] &2Your home has been set!"));
        return true;
    }
}