package World16.TabComplete;

import World16.Main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BackTab implements TabCompleter {

    //Maps
    private Map<String, List<String>> tabCompleteMap;
    //...

    private Main plugin;

    public BackTab(Main plugin) {
        this.plugin = plugin;
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();

        tabCompleteMap.computeIfAbsent("back", k -> new ArrayList<>());

        if (tabCompleteMap.get("back").isEmpty()) {
            tabCompleteMap.get("back").add("death");
            tabCompleteMap.get("back").add("tp");
            tabCompleteMap.get("back").add("set");
            tabCompleteMap.get("back").add("goto");
//            tabCompleteMap.get("back").add("");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("back") || !p.hasPermission("world16.back")) {
            return null;
        }

        if (args.length == 1) {
            return TabUtils.getContainsString(args[0], tabCompleteMap.get("back"));
        }

        return null;
    }
}