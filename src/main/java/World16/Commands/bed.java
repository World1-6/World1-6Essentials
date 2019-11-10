package World16.Commands;

import World16.Main.Main;
import World16.Utils.API;
import World16.Utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class bed implements CommandExecutor {

    private Main plugin;
    private API api;

    public bed(Main getPlugin) {
        this.plugin = getPlugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("bed").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.bed")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        ItemStack item = InventoryUtils.createItem(Material.RED_BED, 1, "Bed", "Bed");
        p.getInventory().addItem(item);
        return true;
    }
}