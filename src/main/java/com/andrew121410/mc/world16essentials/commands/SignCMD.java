package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.blocks.UniversalBlockUtils;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import com.andrew121410.mc.world16utils.utils.TabUtils;
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

import java.util.Arrays;

public class SignCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public SignCMD(World16Essentials plugin) {
        this.plugin = plugin;

        this.api = this.plugin.getApi();
        this.plugin.getCommand("sign").setExecutor(this);
        this.plugin.getCommand("sign").setTabCompleter((commandSender, command, s, args) -> {
            if (args.length == 1)
                return TabUtils.getContainsString(args[0], Arrays.asList("give", "edit"));
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.sign")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("give")) {
            ItemStack itemStack = new ItemStack(Material.SIGN, 1);
            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            player.getInventory().addItem(itemStack);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("edit")) {
            Block block = PlayerUtils.getBlockPlayerIsLookingAt(player);
            BlockState state = block.getState();

            if (!(state instanceof Sign sign)) {
                player.sendMessage(Translate.color("&4Please look at a sign."));
                return true;
            }

            UniversalBlockUtils.editSign(player, sign);
            return true;
        } else {
            player.sendMessage(Translate.color("&cUsage: /sign <give|edit|edit-legacy>"));
        }
        return true;
    }
}
