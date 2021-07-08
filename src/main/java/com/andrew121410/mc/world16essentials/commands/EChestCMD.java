package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.utils.xutils.XSound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EChestCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    public EChestCMD(World16Essentials plugin) {
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
            api.permissionErrorMessage(p);
            return true;
        }

        p.openInventory(p.getEnderChest());
        p.playSound(p.getLocation(), XSound.BLOCK_ENDER_CHEST_OPEN.parseSound(), 10.0f, 1.0f);
        return true;
    }
}
