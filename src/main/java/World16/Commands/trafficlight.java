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

        if (args.length == 0) {
            p.sendMessage(Translate.chat("/trafficlight create <Name>"));
            p.sendMessage(Translate.chat("/trafficlight add <Name> <INT> <INT>"));
            p.sendMessage(Translate.chat("/trafficlight tick <NAME>"));
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            String name = args[1].toLowerCase();
            this.trafficSystemMap.putIfAbsent(name, new TrafficSystem(plugin, TrafficSystemType.ONE_LANE_ROAD));
            this.trafficSystemMap.get(name).getTrafficLightSystemMap().putIfAbsent(0, new TrafficLightSystem(plugin));
            this.trafficSystemMap.get(name).getTrafficLightSystemMap().putIfAbsent(1, new TrafficLightSystem(plugin));
            p.sendMessage(Translate.chat("The trafficlight: " + name + " has been added."));
            return true;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("add")) {
            Block block = api.getBlockPlayerIsLookingAt(p);
            String name = args[1].toLowerCase();
            int system = api.asIntOrDefault(args[2], 0);
            int number = api.asIntOrDefault(args[3], 0);
            TrafficSystem trafficSystem = this.trafficSystemMap.get(name);
            trafficSystem.getTrafficLightSystemMap().get(system).getTrafficLightMap().put(number, new TrafficLight(block.getLocation()));
            p.sendMessage(Translate.chat("ADDED"));
        } else if (args[0].equalsIgnoreCase("tick")) {
            String name = args[1].toLowerCase();
            this.trafficSystemMap.get(name).tick();
            p.sendMessage(Translate.chat("STARTED TICKING"));
        }
        return true;
    }
}
