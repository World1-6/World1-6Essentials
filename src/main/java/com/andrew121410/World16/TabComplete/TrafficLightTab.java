package com.andrew121410.World16.TabComplete;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16TrafficLights.Objects.TrafficSystem;
import com.andrew121410.World16TrafficLights.Objects.TrafficSystemType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TrafficLightTab implements TabCompleter {

    private Map<String, TrafficSystem> trafficSystemMap;
    private Map<String, List<String>> tabCompleteMap;

    private List<String> trafficSystemTypes;

    private Main plugin;
    private API api;

    public TrafficLightTab(Main plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();
        this.tabCompleteMap = this.plugin.getSetListMap().getTabCompleteMap();
        this.trafficSystemTypes = new ArrayList<>();

        tabCompleteMap.computeIfAbsent("trafficlight", k -> new ArrayList<>());

        if (tabCompleteMap.get("trafficlight").isEmpty()) {
            tabCompleteMap.get("trafficlight").add("create");
            tabCompleteMap.get("trafficlight").add("delete");
            tabCompleteMap.get("trafficlight").add("tick");
//            tabCompleteMap.get("trafficlight").add("");
        }

        for (TrafficSystemType value : TrafficSystemType.values()) {
            this.trafficSystemTypes.add(value.name());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String ailis, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player p = (Player) sender;

        if (!cmd.getName().equalsIgnoreCase("trafficlight") || !p.hasPermission("world16.trafficlight")) {
            return null;
        }

        List<String> trafficSystemsList = new ArrayList<>(this.trafficSystemMap.keySet());

        if (args.length == 1) {
            return TabUtils.getContainsString(args[0], tabCompleteMap.get("trafficlight"));
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                return TabUtils.getContainsString(args[1], Arrays.asList("system", "junction", "light"));
            } else if (args.length == 3 && !args[1].equalsIgnoreCase("system")) {
                return TabUtils.getContainsString(args[2], trafficSystemsList);
            } else if (args.length == 4 && args[1].equalsIgnoreCase("system")) {
                return TabUtils.getContainsString(args[3], trafficSystemTypes);
            } else if (args.length == 4) {
                List<String> junctionNames = new ArrayList<>();
                trafficSystemMap.get(args[2]).getTrafficLightSystemMap().forEach((k, v) -> junctionNames.add(String.valueOf(k)));
                return TabUtils.getContainsString(args[3], junctionNames);
            } else if (args.length == 6) {
                return TabUtils.getContainsString(args[5], Arrays.asList("true", "false", "null"));
            }
        } else if (args[0].equalsIgnoreCase("delete")) {
            if (args.length == 2) {
                return TabUtils.getContainsString(args[1], Arrays.asList("system", "junction", "light"));
            } else if (args.length == 3) {
                return TabUtils.getContainsString(args[2], trafficSystemsList);
            } else if (args.length == 4) {
                List<String> junctionNames = new ArrayList<>();
                trafficSystemMap.get(args[2]).getTrafficLightSystemMap().forEach((k, v) -> junctionNames.add(String.valueOf(k)));
                return TabUtils.getContainsString(args[3], junctionNames);
            } else if (args.length == 5) {
                List<String> lights = new ArrayList<>();
                trafficSystemMap.get(args[2]).getTrafficLightSystemMap().get(Integer.valueOf(args[3])).getTrafficLightMap().forEach((k, v) -> lights.add(String.valueOf(k)));
                return TabUtils.getContainsString(args[4], lights);
            }
        } else if (args[0].equalsIgnoreCase("tick")) {
            if (args.length == 2) {
                return TabUtils.getContainsString(args[1], trafficSystemsList);
            }
        }
        return null;
    }
}