package World16.Commands;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.Translate;
import World16FireAlarms.Screen.FireAlarmScreen;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class firealarm implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    //Maps
    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;
    //...

    public firealarm(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.customConfigManager = customConfigManager;
        this.api = new API(this.plugin);

        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();

        this.plugin.getCommand("firealarm").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("world16.firealarm")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("sign")) {
            Block block = api.getBlockPlayerIsLookingAt(p);
            if (block != null)
                this.fireAlarmScreenMap.put(block.getLocation(), new FireAlarmScreen(this.plugin, "test", block.getLocation()));
            p.sendMessage(Translate.chat("The sign has been added to HashMap"));
            return true;
        } else {
            //SOMETHING HERE
            return true;
        }
    }
}