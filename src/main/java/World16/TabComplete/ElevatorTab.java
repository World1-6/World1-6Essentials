package World16.TabComplete;

import World16.Main.Main;
import World16Elevators.Objects.ElevatorObject;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ElevatorTab implements TabCompleter {

    private Main plugin;

    //Maps
    private Map<String, List<String>> tabCompleteMap;
    private Map<String, ElevatorObject> elevatorObjectMap;
    //...

    public ElevatorTab(Main plugin) {
        this.plugin = plugin;
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();

        tabCompleteMap.computeIfAbsent("elevator", k -> new ArrayList<>());

        if (tabCompleteMap.get("elevator").isEmpty()) {
            tabCompleteMap.get("elevator").add("create");
            tabCompleteMap.get("elevator").add("floor");
            tabCompleteMap.get("elevator").add("delete");
            tabCompleteMap.get("elevator").add("call");
            tabCompleteMap.get("elevator").add("stop");
            tabCompleteMap.get("elevator").add("queue");
            tabCompleteMap.get("elevator").add("click");
            tabCompleteMap.get("elevator").add("rename");
//            tabCompleteMap.get("back").add("");
        }

        this.elevatorObjectMap = this.plugin.getSetListMap().getElevatorObjectMap();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alies, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("elevator") || !p.hasPermission("world16.elevator")) {
            return null;
        }

        List<String> elevatorList = new ArrayList<>(this.elevatorObjectMap.keySet());

        if (args.length == 1) {
            return getContains(args[0], tabCompleteMap.get("elevator"));
        } else if (args[0].equalsIgnoreCase("delete")) {
            return getContains(args[1], elevatorList);
        } else if (args[0].equalsIgnoreCase("rename")) {
            return getContains(args[1], elevatorList);
        } else if (args[0].equalsIgnoreCase("floor")) {
            if (args.length == 2) {
                return getContains(args[1], Arrays.asList("easycreate", "delete", "sign"));
            } else if (args.length == 3) {
                return getContains(args[2], elevatorList);
            }
        } else if (args[0].equalsIgnoreCase("call")) {
            if (args.length == 2) {
                return getContains(args[1], elevatorList);
            }
        } else if (args[0].equalsIgnoreCase("stop")) {
            if (args.length == 2) {
                return getContains(args[1], elevatorList);
            }
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