package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class XyzdxdydzCMD implements CommandExecutor {

    private Main plugin;

    private API api;

    public XyzdxdydzCMD(Main plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("xyzdxdydz").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("world16.xyzdxdydz")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&c[xyzdxdydz] Usage: /xyzdxdydz x y z dx dy dz"));
            return true;
        } else if (args.length == 6) {
            String x = args[0];
            String y = args[1];
            String z = args[2];
            String dx = args[3];
            String dy = args[4];
            String dz = args[5];

            if (dx.contains("~")) dx = dx.replace("~", "");
            if (dy.contains("~")) dy = dy.replace("~", "");
            if (dz.contains("~")) dz = dz.replace("~", "");

            String done = "[x=" + x + ",y=" + y + ",z=" + z + ",dx=" + dx + ",dy=" + dy + ",dz=" + dz + "]";
            p.sendMessage(Translate.chat(done));
            BaseComponent[] components = new ComponentBuilder("[CMD] Click me to copy more easier!").event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, done)).create();
            p.spigot().sendMessage(components);
            return true;
        } else {
            p.sendMessage(Translate.chat("&c[xyzdxdydz] Usage: /xyzdxdydz x y z dx dy dz"));
            return true;
        }
    }
}