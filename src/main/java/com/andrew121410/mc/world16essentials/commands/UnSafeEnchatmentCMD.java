package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.ccutils.utils.Utils;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.EnchantmentUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UnSafeEnchatmentCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public UnSafeEnchatmentCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
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
            api.sendPermissionErrorMessage(p);
            return true;
        }

        if (args.length == 2) {
            ItemStack mainHand = p.getInventory().getItemInMainHand();
            Enchantment enchantment = EnchantmentUtils.getByName(args[0]);
            int level = Utils.asIntegerOrElse(args[1], 0);

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
