package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.Utils;
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
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.fs")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.color("&4Usage: &9/fs <&aNumber&9> OR /fs <&cPlayer&9> <&aNumber&9>"));
            player.sendMessage(Translate.color("&6Remember that the default flight speed is &a1"));
            return true;
        } else if (args.length == 1) {
            Double theDouble = Utils.asDoubleOrElse(args[0], null);

            if (theDouble == null) {
                player.sendMessage(Translate.color("&cThat is not a valid number."));
                return true;
            }

            if ((theDouble > -1) && (theDouble < 11)) {
                float flySpeed = (float) (theDouble / 10.0D);
                player.setFlySpeed(flySpeed);
                player.sendMessage(Translate.color("&6Your fly speed has been set to &a" + theDouble));
            } else {
                player.sendMessage(Translate.color("&cYour fly speed must be between &a0 &cand &a10"));
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
                player.sendMessage(Translate.color("&eYou has have set " + target.getDisplayName() + " flight speed too &a" + flySpeed * 10.0F));
            } else {
                player.sendMessage(Translate.color("&cYour fly speed must be between &a0 &cand &a10"));
                return true;
            }
        } else {
            player.sendMessage(Translate.color("&4Usage: &9/fs <&aNumber&9> OR /fs <&cPlayer&9> <&aNumber&9>"));
        }
        return true;
    }
}