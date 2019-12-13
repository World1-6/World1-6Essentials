package World16.Commands.spawn;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Managers.CustomYmlManager;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setspawn implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomYmlManager shitYml;

    public setspawn(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.shitYml = customConfigManager.getShitYml();
        this.api = new API(this.plugin, customConfigManager);

        this.plugin.getCommand("setspawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.setspawn")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        this.api.setLocationToFile(this.shitYml, "Spawn.default", p.getLocation());
        p.sendMessage(Translate.chat("&6Spawn location set for group default."));
        return true;
    }
}