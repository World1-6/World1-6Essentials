package World16.Commands;

import World16.Main.Main;
import World16.Managers.CustomConfigManager;
import World16.Utils.API;
import World16.Utils.Translate;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class unsafenchant implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public unsafenchant(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;

        this.customConfigManager = customConfigManager;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("unsafenchant").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.unsafenchant")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 2) {
            ItemStack mainHand = p.getInventory().getItemInMainHand();
            Enchantment enchantment = Enchantment.getByName(args[0].toLowerCase());
            int level = api.asIntOrDefault(args[1], 0);

            if (enchantment == null) {
                p.sendMessage(Translate.chat("&cLooks like that's not a enchantment."));
                return true;
            }

            mainHand.addUnsafeEnchantment(enchantment, level);
            return true;
        } else {
            p.sendMessage(Translate.chat("&cUsage: &6/unsafenchant <Enchant> <Level>"));
        }
        return true;
    }
}
