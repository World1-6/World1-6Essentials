package com.andrew121410.mc.world16essentials.commands.gamemode;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmsCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public GmsCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("gms").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.gms")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            changeGamemode(player, null);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.gms.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) changeGamemode(target, sender);
            return true;
        } else {
            sender.sendMessage(Translate.miniMessage("<red>Usage: <gold>/gms <player?>"));
        }
        return true;
    }

    private void changeGamemode(Player target, CommandSender sender) {
        String color = target.isOp() ? "&4" : "&7";

        target.setGameMode(GameMode.SURVIVAL);
        target.sendMessage(Translate.chat("&6Set game mode &csurvival&6 for " + color + target.getDisplayName()));
        if (sender != null) {
            sender.sendMessage(Translate.chat("&6Set game mode &csurvival&6 for " + color + target.getDisplayName()));
        }
    }
}