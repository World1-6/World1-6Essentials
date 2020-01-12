package World16.TabComplete;

import World16.Main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DebugTab implements TabCompleter {

    //Maps
    private Map<String, List<String>> tabCompleteMap;
    //...

    private Main plugin;

    public DebugTab(Main plugin) {
        this.plugin = plugin;
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();

        tabCompleteMap.computeIfAbsent("debug1-6", k -> new ArrayList<>());

        if (tabCompleteMap.get("debug1-6").isEmpty()) {
            tabCompleteMap.get("debug1-6").add("op");
            tabCompleteMap.get("debug1-6").add("default");
            tabCompleteMap.get("debug1-6").add("date");
            tabCompleteMap.get("debug1-6").add("reload");
//            tabCompleteMap.get("debug1-6").add("");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("debug1-6") || !p.hasPermission("world16.debug")) {
            return null;
        }

        if (args.length == 1) {
            return TabUtils.getContainsString(args[0], tabCompleteMap.get("debug1-6"));
        }

        return null;
    }
}