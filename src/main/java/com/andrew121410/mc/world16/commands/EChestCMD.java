package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.utils.API;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EChestCMD implements CommandExecutor {

    private Main plugin;
    private API api;

    public EChestCMD(Main plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("echest").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.echest")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        p.openInventory(p.getEnderChest());
        p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 10.0f, 1.0f);
        return true;
    }
}
