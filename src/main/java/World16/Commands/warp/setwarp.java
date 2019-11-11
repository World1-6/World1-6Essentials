package World16.Commands.warp;

import World16.Main.Main;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class setwarp implements CommandExecutor {

    //Maps
    private Map<String, Location> warpsMap;
    //...

    private Main plugin;

    private API api;

    public setwarp(Main plugin) {
        this.plugin = plugin;

        this.api = this.plugin.getApi();

        this.warpsMap = this.plugin.getSetListMap().getWarpsMap();

        this.plugin.getCommand("setwarp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.setwarp")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: &6/setwarp <Name>"));
            return true;
        } else if (args.length == 1) {
            String name = args[0].toLowerCase();
            Location location = p.getLocation();

            if (this.warpsMap.get(name) != null) {
                p.sendMessage(Translate.chat("Looks like there is already a warp with that name..."));
                return true;
            }

            this.warpsMap.putIfAbsent(name, location);
            p.sendMessage(Translate.chat("&6The warp: " + name + " has been set."));
            return true;
        }
        return true;
    }
}