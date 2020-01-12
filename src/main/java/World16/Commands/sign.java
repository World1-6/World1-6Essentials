package World16.Commands;

import World16.Main.Main;
import World16.Utils.API;
import World16.Utils.SignUtils;
import World16.Utils.Translate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class sign implements CommandExecutor {

    private Main plugin;

    private API api;
    private SignUtils signUtils;

    public sign(Main plugin) {
        this.plugin = plugin;

        this.api = new API(this.plugin);
        this.signUtils = new SignUtils(this.plugin);

        this.plugin.getCommand("sign").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.sign")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&e[Sign]&6 Sign help"));
            p.sendMessage(Translate.chat("&6/sign &9<Gives help>"));
            p.sendMessage(Translate.chat("&6/sign give &9<Gives you a sign>"));
            p.sendMessage(Translate.chat("&6/sign edit &9<Edits sign>"));
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("give")) {
            ItemStack item1 = new ItemStack(Material.OAK_SIGN, 1);
            item1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            p.getInventory().addItem(item1);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("edit")) {
            Block block = this.api.getBlockPlayerIsLookingAt(p);
            BlockState state = block.getState();

            if (!(state instanceof Sign)) {
                p.sendMessage(Translate.chat("This isn't a sign."));
                return true;
            }

            Sign sign = (Sign) state;
            signUtils.edit(p, sign);
            return true;
        }
        return true;
    }
}
