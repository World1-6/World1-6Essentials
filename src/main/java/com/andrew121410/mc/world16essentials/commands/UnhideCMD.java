package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class UnhideCMD implements CommandExecutor {

    private final World16Essentials plugin;

    private final List<Player> hiddenPlayers;

    public UnhideCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.hiddenPlayers = this.plugin.getSetListMap().getHiddenPlayers();

        this.plugin.getCommand("unhide").setExecutor(this);
    }

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("world16.hide")) {
            this.plugin.getApi().sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                onlinePlayer.showPlayer(this.plugin, player);
            }
            this.hiddenPlayers.remove(player);
            player.sendMessage(Translate.chat("&6You are now visible to all players."));
        }
        return true;
    }
}