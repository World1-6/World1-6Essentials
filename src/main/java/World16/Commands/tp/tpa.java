package World16.Commands.tp;

import World16.CustomEvents.handlers.TpaEventHandler;
import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class tpa implements CommandExecutor {

    //Maps
    private Map<Player, Player> tpam;
    //...

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public tpa(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = new API(this.plugin);

        this.tpam = this.plugin.getSetListMap().getTpaM();

        this.plugin.getCommand("tpa").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.tpa")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("[&eTPA&r] &cUsage: /tpa <Player>"));
        } else if (args.length == 1) {
            Player target = plugin.getServer().getPlayerExact(args[0]); //Get the player
            if (target != null && target.isOnline()) {
                tpam.put(target, p);
                new TpaEventHandler(this.plugin, p.getDisplayName(), target.getDisplayName()); //RUNS TPA EVENT
                p.sendMessage(Translate.chat("[&eTPA&r] &9Sent tpa request too " + target.getDisplayName()));
                sendTpaRequestMessage(p, target);
            } else {
                p.sendMessage("&4Looks like that player is offline.");
            }
            return true;
        }
        return true;
    }

    private void sendTpaRequestMessage(Player p, Player target) {
        if (p != null && target != null) {
            target.sendMessage(Translate.chat("[&eTPA&r] &a" + p.getDisplayName() + " has sent a tpa request too you."));
            target.sendMessage(Translate.chat("&c/tpaccept &aOR&r &c/tpdeny"));
        }
    }
}