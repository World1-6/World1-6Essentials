package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnSafeEnchatmentCMD implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public UnSafeEnchatmentCMD(Main plugin, CustomConfigManager customConfigManager) {
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
            Enchantment enchantment = this.plugin.getOtherPlugins().getWorld16Utils().getClassWrappers().getEnchantmentUtils().getByName(args[0]);
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
