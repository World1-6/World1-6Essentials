package World16.TabComplete;

import World16.Main.Main;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirealarmTab implements TabCompleter {

    private Main plugin;

    //Maps
    private Map<String, List<String>> tabCompleteMap;
    //...

    //Lists
    private List<String> soundList;
    //...

    public FirealarmTab(Main plugin) {
        this.plugin = plugin;
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();

        tabCompleteMap.computeIfAbsent("firealarm", k -> new ArrayList<>());

        if (tabCompleteMap.get("firealarm").isEmpty()) {
            tabCompleteMap.get("firealarm").add("register");
            tabCompleteMap.get("firealarm").add("delete");
            tabCompleteMap.get("firealarm").add("alarm");
            tabCompleteMap.get("firealarm").add("reset");
            tabCompleteMap.get("firealarm").add("sound");
//            tabCompleteMap.get("firealarm").add("");
        }

        this.soundList = new ArrayList<>();

        for (Sound value : Sound.values()) {
            soundList.add(value.name());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("firealarm")) {
            return null;
        }

        if (!p.hasPermission("world16.firealarm")) {
            return null;
        }

        if (args.length == 1) {
            return getContains(args[0], tabCompleteMap.get("firealarm"));
        } else if (args.length == 3 && args[0].equalsIgnoreCase("sound")) {
            return getContains(args[2], this.soundList);
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
