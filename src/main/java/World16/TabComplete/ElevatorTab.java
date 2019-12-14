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

    //Maps
    private Map<String, List<String>> tabCompleteMap;
    private Map<String, ElevatorObject> elevatorObjectMap;
    //...

    private Main plugin;

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
            tabCompleteMap.get("elevator").add("tostring");
            tabCompleteMap.get("elevator").add("shaft");
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
            return TabUtils.getContainsString(args[0], tabCompleteMap.get("elevator"));
        } else if (args[0].equalsIgnoreCase("delete")) {
            return TabUtils.getContainsString(args[1], elevatorList);
        } else if (args[0].equalsIgnoreCase("rename")) {
            return TabUtils.getContainsString(args[1], elevatorList);
        } else if (args[0].equalsIgnoreCase("floor")) {
            if (args.length == 2) {
                return TabUtils.getContainsString(args[1], Arrays.asList("create", "door", "sign", "delete"));
            } else if (args.length == 3) {
                return TabUtils.getContainsString(args[2], elevatorList);
            }
        } else if (args[0].equalsIgnoreCase("call")) {
            if (args.length == 2) {
                return TabUtils.getContainsString(args[1], elevatorList);
            }
        } else if (args[0].equalsIgnoreCase("shaft")) {
            if (args.length == 2) {
                return TabUtils.getContainsString(args[1], elevatorList);
            } else if (args.length == 3) {
                return TabUtils.getContainsString(args[2], Arrays.asList("ticksPerSecond", "doorHolderTicksPerSecond", "elevatorWaiterTicksPerSecond"));
            }
        } else if (args[0].equalsIgnoreCase("stop")) {
            if (args.length == 2) {
                return TabUtils.getContainsString(args[1], elevatorList);
            }
        } else if (args[0].equalsIgnoreCase("tostring")) {
            if (args.length == 2) {
                return TabUtils.getContainsString(args[1], elevatorList);
            }
        }

        return null;
    }
}