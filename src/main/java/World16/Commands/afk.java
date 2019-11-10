package World16.Commands;

import World16.CustomEvents.handlers.AfkEventHandler;
import World16.CustomEvents.handlers.UnAfkEventHandler;
import World16.Main.Main;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class afk implements CommandExecutor {

    //Lists
    private Map<UUID, Location> afkMap;
    //....

    private Main plugin;
    private API api;

    public afk(Main getPlugin) {
        this.plugin = getPlugin;
        this.api = new API(this.plugin);

        this.afkMap = this.plugin.getSetListMap().getAfkMap();

        this.plugin.getCommand("afk").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.afk")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        String color = "&7";

        if (args.length == 0) {
            if (p.isOp()) color = "&4";
            doAfk(p, color);
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.afk.other")) {
                api.PermissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                if (target.isOp()) color = "&4";
                doAfk(target, color);
            }
            return true;
        } else {
            p.sendMessage(Translate.chat("&cUsage:&9 /afk &aOR &9/afk <Player>"));
        }
        return true;
    }

    private void doAfk(Player player, String color) {
        if (afkMap.get(player.getUniqueId()) == null) {
            this.plugin.getServer().broadcastMessage(Translate.chat("&7* " + color + player.getDisplayName() + "&r&7" + " is now AFK."));
            afkMap.put(player.getUniqueId(), player.getLocation());
            new AfkEventHandler(this.plugin, player.getDisplayName()); //CALLS THE EVENT.
        } else if (afkMap.get(player.getUniqueId()) != null) {
            this.plugin.getServer().broadcastMessage(Translate.chat("&7*" + color + " " + player.getDisplayName() + "&r&7 is no longer AFK."));
            afkMap.remove(player.getUniqueId());
            new UnAfkEventHandler(this.plugin, player.getDisplayName());
        }
    }
}