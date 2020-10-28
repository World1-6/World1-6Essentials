package com.andrew121410.mc.world16.commands.home;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.tabcomplete.HomeListTab;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelhomeCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    public DelhomeCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("delhome").setExecutor(this);
        this.plugin.getCommand("delhome").setTabCompleter(new HomeListTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.home")) {
            api.permissionErrorMessage(p);
            return true;
        }
        String defaultHomeName = "home";

        if (args.length == 1) {
            defaultHomeName = args[0].toLowerCase();

            if (defaultHomeName.equalsIgnoreCase("@allHomes")) {
                this.plugin.getHomeManager().deleteALL(p.getUniqueId());
                return true;
            }
        }

        this.plugin.getHomeManager().delete(p.getUniqueId(), defaultHomeName);
        p.sendMessage(Translate.chat("&9[Homes] &cHome deleted."));
        return true;
    }
}