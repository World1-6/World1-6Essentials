package World16.TabComplete;

import World16.Main.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JailTab implements TabCompleter {

    //Maps
    private Map<String, Location> jailsMap;
    //...

    private Main plugin;

    public JailTab(Main plugin) {
        this.plugin = plugin;

        this.jailsMap = this.plugin.getSetListMap().getJails();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.jail")) {
            return null;
        }

        List<String> keys = new ArrayList<>(this.jailsMap.keySet());

        if (args.length == 1) {
            return TabUtils.getContainsString(args[0], keys);
        }

        return null;
    }
}