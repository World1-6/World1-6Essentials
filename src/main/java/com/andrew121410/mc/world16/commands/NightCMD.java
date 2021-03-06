package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NightCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    public NightCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("night").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.night")) {
            api.permissionErrorMessage(p);
            return true;
        }

        p.getLocation().getWorld().setTime(13000);
        p.sendMessage(Translate.chat("&6The time was set to &9night&r."));
        return true;
    }
}
