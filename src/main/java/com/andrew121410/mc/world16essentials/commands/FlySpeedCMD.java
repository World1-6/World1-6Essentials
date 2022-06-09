package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FlySpeedCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public FlySpeedCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("fs").setExecutor(this);
        this.plugin.getCommand("fs").setTabCompleter((commandSender, command, s, args) -> {
            if (args.length == 1) {
                return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
            }
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

        if (!player.hasPermission("world16.fs")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.chat("&4Usage: &9/fs <&aNumber&9> OR /fs <&cPlayer&9> <&aNumber&9>"));
            player.sendMessage(Translate.chat("&6Remember that the default flight speed is &a1"));
            return true;
        } else if (args.length == 1) {
            Double theDouble = Utils.asDoubleOrElse(args[0], null);

            if (theDouble == null) {
                player.sendMessage(Translate.chat("&cThat isn't a integer..."));
                return true;
            }

            if ((theDouble > -1) && (theDouble < 11)) {
                float flySpeed = (float) (theDouble / 10.0D);
                player.setFlySpeed(flySpeed);
                player.sendMessage(ChatColor.GOLD + "[FlySpeed] " + ChatColor.YELLOW + "Your fly-speed now equals: " + ChatColor.RED + "[" + flySpeed * 10.0F + "]" + ChatColor.YELLOW + "!");
            } else {
                player.sendMessage(ChatColor.GOLD + "[FlySpeed] " + ChatColor.RED + "Your input is not valid! must be between 0 and 10.");
            }
            return true;
        }
        Player target = plugin.getServer().getPlayerExact(args[0]);
        if (target != null && target.isOnline()) {
            if (!player.hasPermission("world16.fs.other")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }
            Double theDouble = Utils.asDoubleOrElse(args[1], null);
            if ((theDouble > -1) && (theDouble < 11)) {
                float flySpeed = (float) (theDouble / 10.0D);
                target.setFlySpeed(flySpeed);
                player.sendMessage(Translate.chat("&eYou has have set " + target.getDisplayName() + " flight speed too &a" + flySpeed * 10.0F));
            } else {
                player.sendMessage(ChatColor.GOLD + "[FlySpeed] " + ChatColor.RED + "Your input is not valid! must be between 0 and 10.");
                return true;
            }
        } else {
            player.sendMessage(Translate.chat("&4Usage: &9/fs <&aNumber&9> OR /fs <&cPlayer&9> <&aNumber&9>"));
        }
        return true;
    }
}