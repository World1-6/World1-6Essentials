package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ColorsCMD implements CommandExecutor {

    private Main plugin;
    private API api;

    public ColorsCMD(Main getPlugin) {
        this.plugin = getPlugin;
        this.api = new API(this.plugin);
        this.plugin.getCommand("colors").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("world16.colors")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        p.sendMessage("Dark Red &4 " + Translate.chat("&4EXAMPLE"));
        p.sendMessage("Red &c " + Translate.chat("&cEXAMPLE"));
        p.sendMessage("Gold &6 " + Translate.chat("&6EXAMPLE"));
        p.sendMessage("Yellow &e " + Translate.chat("&eEXAMPLE"));
        p.sendMessage("Dark Green &2" + Translate.chat("&2EXAMPLE"));
        p.sendMessage("Green &a " + Translate.chat("&aEXAMPLE"));
        p.sendMessage("Aqua &b " + Translate.chat("&bEXAMPLE"));
        p.sendMessage("Dark Aqua &3 " + Translate.chat("&3EXAMPLE"));
        p.sendMessage("Dark Blue &1 " + Translate.chat("&1EXAMPLE"));
        p.sendMessage("Blue &9 " + Translate.chat("&9EXAMPLE"));
        p.sendMessage("Light Purple &d " + Translate.chat("&dEXAMPLE"));
        p.sendMessage("Dark Purple &5 " + Translate.chat("&5EXAMPLE"));
        p.sendMessage("White &f " + Translate.chat("&fEXAMPLE"));
        p.sendMessage("Gray &7 " + Translate.chat("&7EXAMPLE"));
        p.sendMessage("Dark Gray &8 " + Translate.chat("&8EXAMPLE"));
        p.sendMessage("Black &0 " + Translate.chat("&0EXAMPLE"));
        return true;
    }
}
