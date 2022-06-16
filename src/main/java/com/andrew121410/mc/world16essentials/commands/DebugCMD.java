package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.datatranslator.DataTranslator;
import com.andrew121410.mc.world16essentials.datatranslator.Software;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class DebugCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final CustomConfigManager customConfigManager;
    private final API api;

    public DebugCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("debug1-6").setExecutor(this);
        this.plugin.getCommand("debug1-6").setTabCompleter((sender, command, s, args) -> {
            if (!(sender instanceof Player player)) return null;
            if (!player.hasPermission("world16.debug")) return null;

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], Arrays.asList("reload", "load", "unload", "convert"));
            } else if (args[0].equalsIgnoreCase("convert")) {
                if (args.length == 2) {
                    return TabUtils.getContainsString(args[1], Arrays.asList("from", "to"));
                } else if (args.length == 3) {
                    List<String> typesOfSoftwareList = Arrays.stream(Software.values()).map(Enum::name).toList();
                    return TabUtils.getContainsString(args[2], typesOfSoftwareList);
                }
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

        if (!player.hasPermission("world16.debug")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.chat("&6Please use tab complete."));
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            player.sendMessage(Translate.chat("Reloading all configs might lag."));
            this.customConfigManager.reloadAll();
            player.sendMessage(Translate.chat("All configs are reloaded."));
            return true;
        } else if (args[0].equalsIgnoreCase("load")) {
            if (args.length == 1) {
                player.sendMessage(Translate.chat("Loading your data. PLEASE WAIT"));
                this.plugin.getPlayerInitializer().unload(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getPlayerInitializer().load(player);
                        player.sendMessage(Translate.chat("Data has been successfully loaded"));
                    }
                }.runTaskLater(plugin, 20);
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("@all")) {
                player.sendMessage(Translate.chat("&6Loading everyones data. PLEASE WAIT"));
                for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                    this.plugin.getPlayerInitializer().load(onlinePlayer);
                    onlinePlayer.sendMessage(Translate.chat("Your player data has been &aLOADED&r by [" + player.getDisplayName() + "]"));
                }
                player.sendMessage(Translate.chat("&aEveryones player data has been loaded."));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("unload")) {
            if (args.length == 1) {
                player.sendMessage(Translate.chat("Unloading your player data PLEASE WAIT."));
                this.plugin.getPlayerInitializer().unload(player);
                player.sendMessage(Translate.chat("Your data has been unloaded."));
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("@all")) {
                player.sendMessage(Translate.chat("Unloading everyones player data PLEASE WAIT."));
                for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                    this.plugin.getPlayerInitializer().unload(onlinePlayer);
                    onlinePlayer.sendMessage(Translate.chat("Your player data has been &cUNLOADED&r by [" + player.getDisplayName() + "]"));
                }
                player.sendMessage(Translate.chat("&aEveryones data has been unloaded."));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("convert")) {
            if (args.length == 3) {
                DataTranslator dataTranslator = new DataTranslator(this.plugin);
                Software software = null;

                try {
                    software = Software.valueOf(args[2]);
                } catch (Exception ignored) {
                }

                if (args[1].equalsIgnoreCase("from") && software != null) {
                    dataTranslator.convertFrom(software);
                } else if (args[1].equalsIgnoreCase("to") && software != null) {
                    dataTranslator.convertTo(software);
                }
            }
        }
        return true;
    }
}