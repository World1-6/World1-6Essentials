package World16.Commands;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.Translate;
import World16TrafficLights.Objects.TrafficLight;
import World16TrafficLights.Objects.TrafficLightSystem;
import World16TrafficLights.Objects.TrafficSystem;
import World16TrafficLights.Objects.TrafficSystemType;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class trafficlight implements CommandExecutor {

    private Map<String, TrafficSystem> trafficSystemMap;

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public trafficlight(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.trafficSystemMap = this.plugin.getSetListMap().getTrafficSystemMap();

        this.customConfigManager = customConfigManager;

        this.plugin.getCommand("trafficlight").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.trafficlight")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (!api.isTrafficSystemEnabled()) {
            p.sendMessage(Translate.chat("Looks like the traffic system isn't enabled."));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("/trafficlight create [SHOWS HELP TO CREATE"));
            p.sendMessage(Translate.chat("/trafficlight tick <Name>"));
            return true;
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("/trafficlight create system <Name> <Type>"));
                p.sendMessage(Translate.chat("/trafficlight create junction <Name> <Int> <isTurningJunction"));
                p.sendMessage(Translate.chat(""));
                p.sendMessage(Translate.chat("/trafficlight create light <Name> <Junction> <Int> <O isLeft"));
                return true;
            } else if (args[1].equalsIgnoreCase("system")) {
                String name = args[2].toLowerCase();
                String rawType = args[3];

                TrafficSystemType trafficSystemType;
                try {
                    trafficSystemType = TrafficSystemType.valueOf(rawType);
                } catch (Exception e) {
                    p.sendMessage(Translate.chat("Not a valid TrafficSystemType"));
                    return true;
                }

                if (this.trafficSystemMap.get(name) != null) {
                    p.sendMessage(Translate.chat("Looks like that traffic light system already exists with that name."));
                    return true;
                }

                this.trafficSystemMap.put(name, new TrafficSystem(plugin, trafficSystemType));
                p.sendMessage(Translate.chat(name + " traffic system has been added."));
                return true;
            } else if (args[1].equalsIgnoreCase("junction")) {
                String name = args[2].toLowerCase();
                int key = api.asIntOrDefault(args[3], 0);
                boolean isturningJunction = api.asBooleanOrDefault(args[4], false);

                if (this.trafficSystemMap.get(name) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a valid traffic system"));
                    return true;
                }

                this.trafficSystemMap.get(name).getTrafficLightSystemMap().putIfAbsent(key, new TrafficLightSystem(plugin, isturningJunction));
                p.sendMessage(Translate.chat("Junction box has been added to: " + name));
                return true;
            } else if (args[1].equalsIgnoreCase("light")) {
                Block block = api.getBlockPlayerIsLookingAt(p);
                String name = args[2].toLowerCase();
                int junctionName = api.asIntOrDefault(args[3], 0);
                int number = api.asIntOrDefault(args[4], 0);
                Boolean isLeft = Boolean.valueOf(args[5]);

                if (this.trafficSystemMap.get(name) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a valid traffic system"));
                    return true;
                }

                if (this.trafficSystemMap.get(name).getTrafficLightSystemMap().get(junctionName) == null) {
                    p.sendMessage(Translate.chat("Looks like that isn't a valid junction"));
                    return true;
                }

                this.trafficSystemMap.get(name).getTrafficLightSystemMap().get(junctionName).getTrafficLightMap().put(number, new TrafficLight(block.getLocation(), isLeft));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("tick")) {
            String name = args[1].toLowerCase();

            if (this.trafficSystemMap.get(name) == null) {
                p.sendMessage(Translate.chat("Looks like that isn't a valid traffic system"));
                return true;
            }

            this.trafficSystemMap.get(name).tick();
            p.sendMessage(Translate.chat(name + " has started ticking"));
            return true;
        }
        return true;
    }
}
