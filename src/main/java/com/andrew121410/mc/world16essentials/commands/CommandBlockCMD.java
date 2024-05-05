package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBlockCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public CommandBlockCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("commandblock").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.commandblock")) {
            api.sendPermissionErrorMessage(p);
            return true;
        }

        ItemStack item = InventoryUtils.createItem(Material.COMMAND_BLOCK, 1, "&cCommand Block&r", "New Fresh Command Block");
//        item.addUnsafeEnchantment(Enchantment.SHARPNESS, 1);
        api.TEMP_AddunsafeEnchantment_SHARPNESS(item);
        p.getInventory().addItem(item);
        return true;
    }
}
