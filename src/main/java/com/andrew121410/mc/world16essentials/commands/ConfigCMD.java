package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
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
                return TabUtils.getContainsString(args[0], Arrays.asList("signTranslateColors", "preventCropsTrampling", "spawnMobCap", "messages"));
            } else if (args[0].equalsIgnoreCase("signTranslateColors")
                    || args[0].equalsIgnoreCase("preventCropsTrampling")) {
                return TabUtils.getContainsString(args[1], Arrays.asList("true", "false"));
            } else if (args[0].equalsIgnoreCase("spawnMobCap")) {
                return TabUtils.getContainsString(args[1], Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
            } else if (args[0].equalsIgnoreCase("messages")) {
                if (args.length == 2) {
                    return TabUtils.getContainsString(args[1], Arrays.asList("prefix", "welcomeBackMessage", "firstJoinedMessage", "leaveMessage"));
                } else {
                    if (args[1].equalsIgnoreCase("prefix")) {
                        return TabUtils.getContainsString(args[2], Collections.singletonList(api.getMessagesUtils().getPrefix()));
                    } else if (args[1].equalsIgnoreCase("welcomeBackMessage")) {
                        return TabUtils.getContainsString(args[2], Collections.singletonList(api.getMessagesUtils().getWelcomeBackMessage()));
                    } else if (args[1].equalsIgnoreCase("firstJoinedMessage")) {
                        return TabUtils.getContainsString(args[2], Collections.singletonList(api.getMessagesUtils().getFirstJoinedMessage()));
                    } else if (args[1].equalsIgnoreCase("leaveMessage")) {
                        return TabUtils.getContainsString(args[2], Collections.singletonList(api.getMessagesUtils().getLeaveMessage()));
                    }
                }
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
            if (args[0].equalsIgnoreCase("signTranslateColors")) {
                api.getConfigUtils().setSignTranslateColors(args[1].equalsIgnoreCase("true"));
                player.sendMessage(Translate.color("&aSign translate colors set to &6" + args[1]));
            } else if (args[0].equalsIgnoreCase("preventCropsTrampling")) {
                api.getConfigUtils().setPreventCropsTrampling(args[1].equalsIgnoreCase("true"));
                player.sendMessage(Translate.color("&aPrevent crops trampling set to &6" + args[1]));
            } else if (args[0].equalsIgnoreCase("spawnMobCap")) {
                int spawnMobCap = Utils.asIntegerOrElse(args[1], 1);
                api.getConfigUtils().setSpawnMobCap(spawnMobCap);
                player.sendMessage(Translate.color("&aSpawn mob cap set to &6" + spawnMobCap));
            }
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("messages")) {
            String[] ourArgs = Arrays.copyOfRange(args, 2, args.length);
            String message = String.join(" ", ourArgs);
            if (args[1].equalsIgnoreCase("prefix")) {
                api.getMessagesUtils().setPrefix(message);
                player.sendMessage(Translate.color("&aPrefix set to &r" + message));
            } else if (args[1].equalsIgnoreCase("welcomeBackMessage")) {
                api.getMessagesUtils().setWelcomeBackMessage(message);
                player.sendMessage(Translate.color("&aWelcome back message set to &r" + message));
            } else if (args[1].equalsIgnoreCase("firstJoinMessage")) {
                api.getMessagesUtils().setFirstJoinedMessage(message);
                player.sendMessage(Translate.color("&aFirst join message set to &r" + message));
            } else if (args[1].equalsIgnoreCase("leaveMessage")) {
                api.getMessagesUtils().setLeaveMessage(message);
                player.sendMessage(Translate.color("&aLeave message set to &r" + message));
            }
        }
        return true;
    }
}
