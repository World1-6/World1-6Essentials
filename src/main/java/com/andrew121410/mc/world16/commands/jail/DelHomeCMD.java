package com.andrew121410.mc.world16.commands.jail;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.managers.JailManager;
import com.andrew121410.mc.world16.tabcomplete.JailTab;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCMD implements CommandExecutor {

    private Main plugin;

    private API api;
    private JailManager jailManager;

    public DelHomeCMD(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.api = new API(this.plugin);
        this.jailManager = this.plugin.getJailManager();

        this.plugin.getCommand("deljail").setExecutor(this);
        this.plugin.getCommand("deljail").setTabCompleter(new JailTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.deljail")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage:&6 /deljail <Name>"));
            return true;
        } else if (args.length == 1) {
            String jailName = args[0].toLowerCase();

            if (!jailManager.delete(jailName)) {
                p.sendMessage(Translate.chat("That's not a jail."));
                return true;
            }

            p.sendMessage(Translate.chat("&eJail: " + jailName + " has been deleted."));
            return true;
        }
        return true;
    }
}
