package com.andrew121410.World16.Commands.tp;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.CustomConfigManager;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class tpdeny implements CommandExecutor {

    //Maps
    private Map<Player, Player> tpam;
    //...


    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public tpdeny(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = new API(this.plugin);

        this.tpam = this.plugin.getSetListMap().getTpaM();

        this.plugin.getCommand("tpdeny").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.tpdeny")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            Player tpa = this.tpam.get(p);
            if (tpa != null) {
                p.sendMessage(Translate.chat("&9Ok you denied the tp request."));
                tpa.sendMessage(Translate.chat("[&eTPA&r] &cYour tpa request got denied by " + p.getDisplayName()));
                this.tpam.remove(p);
            } else {
                p.sendMessage(Translate.chat("&4Something went wrong."));
            }
        } else {
            p.sendMessage(Translate.chat("&4???"));
        }
        return true;
    }
}