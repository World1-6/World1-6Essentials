package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16essentials.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.xutils.XMaterial;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BedCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public BedCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = this.plugin.getApi();

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
            api.sendPermissionErrorMessage(p);
            return true;
        }

        ItemStack item = InventoryUtils.createItem(XMaterial.RED_BED.parseMaterial(), 1, "Bed", "Bed");
        p.getInventory().addItem(item);
        return true;
    }
}