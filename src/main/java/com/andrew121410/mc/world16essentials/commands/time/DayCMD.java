package com.andrew121410.mc.world16essentials.commands.time;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public DayCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("day").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.day")) {
            api.sendPermissionErrorMessage(p);
            return true;
        }

        p.getLocation().getWorld().setTime(500);
        p.sendMessage(Translate.color("&6The time was set to &eday&r."));
        return true;
    }
}