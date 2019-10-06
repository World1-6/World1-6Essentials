package World16.Commands.jail;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.JailManager;
import World16.TabComplete.JailTab;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class jail implements CommandExecutor {

    //Maps
    private Map<String, Location> jailsMap;
    //...

    private Main plugin;
    private API api;
    private JailManager jailManager;

    public jail(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.jailsMap = this.plugin.getSetListMap().getJails();

        this.api = new API(this.plugin);
        this.jailManager = this.plugin.getJailManager();

        this.plugin.getCommand("jail").setExecutor(this);
        this.plugin.getCommand("jail").setTabCompleter(new JailTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.jail")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: &6/jail <Name>"));
            return true;
        } else if (args.length == 1) {
            String jailName = args[0].toLowerCase();

            if (jailsMap.get(jailName) == null) {
                p.sendMessage(Translate.chat("That's not a jail."));
                return true;
            }

            p.teleport(jailsMap.get(jailName));
            p.sendMessage(Translate.chat("&6Teleporting..."));
            return true;
        }
        return true;
    }
}
