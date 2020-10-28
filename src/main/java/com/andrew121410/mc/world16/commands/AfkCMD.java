package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AfkCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    public AfkCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("afk").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.afk")) {
            api.permissionErrorMessage(p);
            return true;
        }

        String color = "&7";

        if (args.length == 0) {
            if (p.isOp()) color = "&4";
            api.doAfk(p, color);
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.afk.other")) {
                api.permissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                if (target.isOp()) color = "&4";
                api.doAfk(target, color);
            }
            return true;
        } else {
            p.sendMessage(Translate.chat("&cUsage:&9 /afk &aOR &9/afk <Player>"));
        }
        return true;
    }
}