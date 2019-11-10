package World16.Commands;

import World16.Main.Main;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class feed implements CommandExecutor {

    private Main plugin;
    private API api;

    public feed(Main plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("feed").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.feed")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.setFoodLevel(20);
            p.sendMessage(Translate.chat("&6There you go."));
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.feed.other")) {
                api.PermissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                target.setFoodLevel(20);
                p.sendMessage(Translate.chat("&6There you go you just feed " + target.getDisplayName()));
            }
            return true;
        } else {
            p.sendMessage(Translate.chat("&cUsage: for yourself do /feed OR /feed <Player>"));
        }
        return true;
    }
}