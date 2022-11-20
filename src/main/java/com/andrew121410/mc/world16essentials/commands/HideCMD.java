package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HideCMD implements CommandExecutor {

    private final World16Essentials plugin;

    private final List<UUID> hiddenPlayers;

    public HideCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.hiddenPlayers = this.plugin.getSetListMap().getHiddenPlayers();

        this.plugin.getCommand("hide").setExecutor(this);
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }

        if (!player.hasPermission("world16.hide")) {
            this.plugin.getApi().sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                onlinePlayer.hidePlayer(this.plugin, player);
            }
            this.hiddenPlayers.add(player.getUniqueId());
            player.sendMessage(Translate.chat("&6You are now hidden from all players."));
        }
        return true;
    }
}