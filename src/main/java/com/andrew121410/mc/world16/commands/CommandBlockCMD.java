package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16.utils.InventoryUtils;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandBlockCMD implements CommandExecutor {

    private Main plugin;
    private API api;

    public CommandBlockCMD(Main getPlugin) {
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
            api.PermissionErrorMessage(p);
            return true;
        }

        ItemStack item = InventoryUtils.createItem(XMaterial.COMMAND_BLOCK.parseMaterial(), 1, "&cCommand Block&r", "New Fresh Command Block");
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        p.getInventory().addItem(item);
        return true;
    }
}
