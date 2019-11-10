package World16.TabComplete;

import World16.Main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ERamTab implements TabCompleter {

    //Maps
    private Map<String, List<String>> tabCompleteMap;
    //...

    private Main plugin;

    public ERamTab(Main plugin) {
        this.plugin = plugin;
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();

        tabCompleteMap.computeIfAbsent("eram", k -> new ArrayList<>());

        if (tabCompleteMap.get("eram").isEmpty()) {
            tabCompleteMap.get("eram").add("save");
            tabCompleteMap.get("eram").add("load");
            tabCompleteMap.get("eram").add("do");
            tabCompleteMap.get("eram").add("undo");
            tabCompleteMap.get("eram").add("list");
            tabCompleteMap.get("eram").add("delete");
            tabCompleteMap.get("eram").add("addtag");
            tabCompleteMap.get("eram").add("deletetag");
//            tabCompleteMap.get("eram").add("");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("eram") || !p.hasPermission("world16.eram")) {
            return null;
        }

        if (args.length == 1) {
            return getContains(args[0], tabCompleteMap.get("eram"));
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
