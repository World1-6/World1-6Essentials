package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnSafeEnchatmentCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public UnSafeEnchatmentCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("unsafenchant").setExecutor(this);
        this.plugin.getCommand("unsafenchant").setTabCompleter((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.unsafenchant")) return null;

            // Broken
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("world16.unsafenchant")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        player.sendMessage(Translate.color("&cThis feature doesn't support 1.12.2"));
        return true;
    }
}
