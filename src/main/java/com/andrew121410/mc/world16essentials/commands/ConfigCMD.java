package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class ConfigCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public ConfigCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("config1-6").setExecutor(this);
        this.plugin.getCommand("config1-6").setTabCompleter((sender, command, s, args) -> {
            if (!(sender instanceof Player player)) return null;
            if (!player.hasPermission("world16.config")) return null;

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], Arrays.asList("setprefix", "signTranslateColors", "preventCropsTrampling"));
            } else if (args[0].equalsIgnoreCase("setprefix")) {
                return TabUtils.getContainsString(args[1], Collections.singletonList(api.getPrefix()));
            } else if (args[0].equalsIgnoreCase("signTranslateColors")) {
                return TabUtils.getContainsString(args[1], Arrays.asList("true", "false"));
            } else if (args[0].equalsIgnoreCase("preventCropsTrampling")) {
                return TabUtils.getContainsString(args[1], Arrays.asList("true", "false"));
            }
            return null;
        });
    }

    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }

        if (!player.hasPermission("world16.config")) {
            this.plugin.getApi().sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.color("&cUsage: /config1-6 <config> <value>"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setprefix")) {
                api.setPrefix(args[1]);
                player.sendMessage(Translate.color("&aPrefix set to &6" + args[1]));
            } else if (args[0].equalsIgnoreCase("signTranslateColors")) {
                api.setSignTranslateColors(args[1].equalsIgnoreCase("true"));
                player.sendMessage(Translate.color("&aSign translate colors set to &6" + args[1]));
            } else if (args[0].equalsIgnoreCase("preventCropsTrampling")) {
                api.setPreventCropsTrampling(args[1].equalsIgnoreCase("true"));
                player.sendMessage(Translate.color("&aPrevent crops trampling set to &6" + args[1]));
            }
        }
        return true;
    }
}
