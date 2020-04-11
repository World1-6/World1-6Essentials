package com.andrew121410.World16.Commands;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.CustomConfigManager;
import com.andrew121410.World16.TabComplete.DebugTab;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class debug implements CommandExecutor {

    private Main plugin;
    private CustomConfigManager customConfigManager;
    private API api;

    public debug(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("debug1-6").setExecutor(this);
        this.plugin.getCommand("debug1-6").setTabCompleter(new DebugTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.debug")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&6Please use tab complete."));
            return true;
        } else if (args[0].equalsIgnoreCase("op")) {
            if (!p.hasPermission("world16.debug.op")) { // Permission
                api.PermissionErrorMessage(p);
                return true;
            }
            p.sendMessage(Translate.chat("&4Debug working oping andrew and tyler and richard"));
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Set<String> opSet = new HashSet<>();
            opSet.add("AlphaGibbon43");
            opSet.add("Robobros3");
            opSet.add("andrew121410");
            opSet.forEach(set -> this.plugin.getServer().dispatchCommand(console, "op " + set));
            return true;
        } else if (args.length == 1 && (args[0].equalsIgnoreCase("default"))) {
            if (!p.hasPermission("world16.debug.defaultstuff")) { // Permission
                api.PermissionErrorMessage(p);
                return true;
            }
            this.plugin.getConfig().set("TittleTOP", "&f&l[&4World 1-6&f&l]");
            this.plugin.getConfig().set("TittleBOTTOM", "&9&oHome Of Minecraft Fire Alarms.");
            this.plugin.getConfig().set("TablistTOP", "&f&l[&4World 1-6&f&l]");
            this.plugin.getConfig().set("TablistBOTTOM", "&9&oHome Of Minecraft Fire Alarms.");
            this.plugin.saveConfig();
            this.plugin.reloadConfig();
            p.sendMessage(Translate.chat("&bOK..."));
            return true;
            //DATE
        } else if (args.length == 1 && (args[0].equalsIgnoreCase("date"))) {
            if (!p.hasPermission("world16.debug.date")) { // Permission
                api.PermissionErrorMessage(p);
                return true;
            }
            String date = api.Time();
            p.sendMessage(Translate.chat("Time/Data:-> " + date));
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            p.sendMessage(Translate.chat("Reloading all configs might lag."));
            this.customConfigManager.reloadAll();
            p.sendMessage(Translate.chat("All configs are reloaded."));
            return true;
        } else if (args[0].equalsIgnoreCase("load")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("Loading your data. PLEASE WAIT"));
                this.plugin.getPlayerInitializer().unload(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.getPlayerInitializer().load(p);
                        p.sendMessage(Translate.chat("Data has been successfully loaded"));
                    }
                }.runTaskLater(plugin, 20);
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("@all")) {
                p.sendMessage(Translate.chat("&6Loading everyones data. PLEASE WAIT"));
                for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                    this.plugin.getPlayerInitializer().load(onlinePlayer);
                    onlinePlayer.sendMessage(Translate.chat("Your player data has been &aLOADED&r by [" + p.getDisplayName() + "]"));
                }
                p.sendMessage(Translate.chat("&aEveryones player data has been loaded."));
                return true;
            }
        } else if (args[0].equalsIgnoreCase("unload")) {
            if (args.length == 1) {
                p.sendMessage(Translate.chat("Unloading your player data PLEASE WAIT."));
                this.plugin.getPlayerInitializer().unload(p);
                p.sendMessage(Translate.chat("Your data has been unloaded."));
                return true;
            } else if (args.length == 2 && args[1].equalsIgnoreCase("@all")) {
                p.sendMessage(Translate.chat("Unloading everyones player data PLEASE WAIT."));
                for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
                    this.plugin.getPlayerInitializer().unload(onlinePlayer);
                    onlinePlayer.sendMessage(Translate.chat("Your player data has been &cUNLOADED&r by [" + p.getDisplayName() + "]"));
                }
                p.sendMessage(Translate.chat("&aEveryones data has been unloaded."));
                return true;
            }
        }
        return true;
    }
}