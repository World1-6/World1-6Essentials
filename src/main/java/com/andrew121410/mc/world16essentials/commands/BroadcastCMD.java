package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCMD implements CommandExecutor {

    private World16Essentials plugin;

    private API api;

    public BroadcastCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("broadcast").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.broadcast")) {
            api.permissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("[&cBroadCast&r] &cUsage: /broadcast <Message>"));
            return true;
        } else {
            this.plugin.getServer().getOnlinePlayers().stream().forEach(player -> player.sendMessage(Translate.chat("[&c&lBroadcast&r]&a {messager}").replace("{messager}", String.join(" ", args))));
            return true;
        }
    }
}