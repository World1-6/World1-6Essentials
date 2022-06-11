package com.andrew121410.mc.world16essentials.commands.home;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.sharedtabcomplete.HomeListTab;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelhomeCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public DelhomeCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("delhome").setExecutor(this);
        this.plugin.getCommand("delhome").setTabCompleter(new HomeListTab(this.plugin));
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

            if (defaultHomeName.equalsIgnoreCase("@allHomes")) {
                this.plugin.getHomeManager().deleteALL(player.getUniqueId());
                return true;
            }
        }

        this.plugin.getHomeManager().delete(player.getUniqueId(), defaultHomeName);
        player.sendMessage(Translate.chat("&9[Homes] &cHome deleted."));
        return true;
    }
}