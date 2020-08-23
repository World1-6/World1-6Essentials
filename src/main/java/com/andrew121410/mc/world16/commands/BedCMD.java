package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.xutils.XMaterial;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BedCMD implements CommandExecutor {

    private Main plugin;
    private API api;

    public BedCMD(Main getPlugin) {
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

        ItemStack item = InventoryUtils.createItem(XMaterial.RED_BED.parseMaterial(), 1, "Bed", "Bed");
        p.getInventory().addItem(item);
        return true;
    }
}