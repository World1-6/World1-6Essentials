package World16.Commands.tp;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class tpaccept implements CommandExecutor {

    private Main plugin;

    private API api;

    //Maps
    private Map<Player, Player> tpam;
    //...

    private CustomConfigManager customYmlManager;

    public tpaccept(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customYmlManager = customConfigManager;
        this.api = new API(this.plugin);

        this.tpam = this.plugin.getSetListMap().getTpaM();

        this.plugin.getCommand("tpaccept").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("world16.tpaccept")) {
            api.PermissionErrorMessage(p);
            return true;
        }
        if (args.length == 0) {
            Player tpa = this.tpam.get(p);
            if (tpa != null) {
                tpa.teleport(p);
                tpa.sendMessage(Translate.chat("[&eTPA&r] &a" + p.getDisplayName() + " has accepted your tpa request."));
                this.tpam.remove(p);
                return true;
            } else {
                p.sendMessage(Translate.chat("&e[TPA]&r &cLooks like you don't have any tpa request."));
            }
            return true;
        }
        return true;
    }
}