package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RamCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public RamCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("ram").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("world16.ram")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        long maxMemory = (Runtime.getRuntime().maxMemory() / 1024 / 1024);
        long allocatedMemory = (Runtime.getRuntime().totalMemory() / 1024 / 1024);
        long freeMemory = (Runtime.getRuntime().freeMemory() / 1024 / 1024);
        long usedMemory = allocatedMemory - freeMemory;

        player.sendMessage(Translate.chat("&6Maximum memory: &c" + maxMemory + " MB."));
        player.sendMessage(Translate.chat("&6Allocated memory: &c" + allocatedMemory + " MB."));
        player.sendMessage(Translate.chat("&6Free memory: &c" + freeMemory + " MB."));
        player.sendMessage(Translate.chat("&6Used memory: &c" + usedMemory + " MB."));
        return true;
    }
}
