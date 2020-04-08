package com.andrew121410.World16.Commands.home;

import com.andrew121410.CCUtils.storage.ISQL;
import com.andrew121410.CCUtils.storage.SQLite;
import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.HomeManager;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class sethome implements CommandExecutor {

    private Main plugin;

    private ISQL isql;
    private HomeManager homeManager;
    private API api;

    public sethome(Main plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        isql = new SQLite(this.plugin.getDataFolder(), "Homes");
        homeManager = new HomeManager(this.plugin, this.isql);
        plugin.getCommand("sethome").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.home")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        String defaultHomeName = "home";

        if (args.length == 1) {
            defaultHomeName = args[0].toLowerCase();
        }

        homeManager.setHome(isql, p, defaultHomeName);
        p.sendMessage(Translate.chat("&9[Homes] &2Your home has been set!"));
        return true;
    }
}