package World16.Commands;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class lastjoin implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public lastjoin(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.customConfigManager = customConfigManager;
        this.api = new API(this.plugin);

        this.plugin.getCommand("lastjoin").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("world16.lastjoin")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: /lastjoin <Player>"));
            return true;
        } else if (args.length == 1) {
            UUID uuid = this.api.getUUIDFromMojangAPI(args[0]);

            if (uuid == null) {
                p.sendMessage(Translate.chat("Looks like that isn't a player."));
                return true;
            }

            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(uuid);

            if (!offlinePlayer.hasPlayedBefore()) {
                p.sendMessage(Translate.chat("Looks like that player has never joined the server."));
                return true;
            }

            String time = api.convertTime(offlinePlayer);
            p.sendMessage(Translate.chat("Player: " + offlinePlayer.getName() + " has last joined it " + time));
            return true;
        }
        return true;
    }
}
