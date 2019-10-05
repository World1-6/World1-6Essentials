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

public class WarpTab implements TabCompleter {

    //Maps
    private Map<String, Location> warpsMap;
    //...

    private Main plugin;

    public WarpTab(Main plugin) {
        this.plugin = plugin;

        this.warpsMap = this.plugin.getSetListMap().getWarpsMap();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.warp")) {
            return null;
        }

        List<String> keys = new ArrayList<>(this.warpsMap.keySet());

        if (args.length == 1) {
            return getContains(args[0], keys);
        }

        return null;
    }

    private List<String> getContains(String args, List<String> oldArrayList) {
        List<String> list = new ArrayList<>();

        for (String mat : oldArrayList) {
            if (mat.contains(args.toLowerCase())) {
                list.add(mat);
            }
        }

        return list;
    }
}
