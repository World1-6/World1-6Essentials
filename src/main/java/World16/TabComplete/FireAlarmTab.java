package World16.TabComplete;

import World16.Main.Main;
import World16FireAlarms.interfaces.IFireAlarm;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FireAlarmTab implements TabCompleter {

    private Main plugin;

    //Maps
    private Map<String, List<String>> tabCompleteMap;
    private Map<String, IFireAlarm> iFireAlarmMap;
    //...

    //Lists
    private List<String> soundList;
    //...

    public FireAlarmTab(Main plugin) {
        this.plugin = plugin;

        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();
        this.iFireAlarmMap = this.plugin.getSetListMap().getFireAlarmMap();

        tabCompleteMap.computeIfAbsent("firealarm", k -> new ArrayList<>());

        if (tabCompleteMap.get("firealarm").isEmpty()) {
            tabCompleteMap.get("firealarm").add("register");
            tabCompleteMap.get("firealarm").add("delete");
            tabCompleteMap.get("firealarm").add("alarm");
            tabCompleteMap.get("firealarm").add("reset");
            tabCompleteMap.get("firealarm").add("sound");
//            tabCompleteMap.get("firealarm").add("");
        }

        this.soundList = this.plugin.getSetListMap().getSoundsList();

        for (Sound value : Sound.values()) {
            this.soundList.add(value.name());
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

        List<String> firealarmList = new ArrayList<>(this.iFireAlarmMap.keySet());

        if (args.length == 1) {
            return getContains(args[0], tabCompleteMap.get("firealarm"));
        } else if (args[0].equalsIgnoreCase("register")) {
            if (args.length == 2) {
                return getContains(args[1], Arrays.asList("firealarm", "sign", "strobe"));
            } else if (args.length == 3 && args[1].equalsIgnoreCase("sign") || args[1].equalsIgnoreCase("strobe")) {
                return getContains(args[2], firealarmList);
            }
            return null;
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 2) {
                return getContains(args[1], Arrays.asList("firealarm", "strobe"));
            } else if (args.length == 3) {
                return getContains(args[2], firealarmList);
            }
            return null;
        } else if (args[0].equalsIgnoreCase("sound")) {
            if (args.length == 2) {
                return getContains(args[1], firealarmList);
            } else if (args.length == 3) {
                return getContains(args[2], this.soundList);
            }
            return null;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reset")) {
            return getContains(args[1], firealarmList);
        } else if (args[0].equalsIgnoreCase("alarm")) {
            if (args.length == 2) {
                return getContains(args[1], Arrays.asList("test", "ps"));
            } else if (args.length == 3) {
                return getContains(args[2], firealarmList);
            }
            return null;
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
