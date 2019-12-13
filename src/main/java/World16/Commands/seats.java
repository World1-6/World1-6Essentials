package World16.Commands;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class seats implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public seats(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.customConfigManager = customConfigManager;

        this.plugin.getCommand("seats").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.seats")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: &6/seats <TRUEorFALSE &9[FALSE will turn off the seats for your Player.]"));
            return true;
        } else if (args.length == 1) {
            boolean enabled = api.asBooleanOrDefault(args[0], true);

            api.getPlayersYML(this.customConfigManager, p).set("seats", enabled);
            p.sendMessage(Translate.chat("Seats has been set to: " + enabled));
            return true;
        }
        return true;
    }
}
