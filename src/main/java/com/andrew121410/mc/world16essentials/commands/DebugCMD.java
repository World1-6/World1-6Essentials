package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.tabcomplete.DebugTab;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16essentials.utils.DataTranslator;
import com.andrew121410.mc.world16essentials.utils.Software;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class DebugCMD implements CommandExecutor {

    private World16Essentials plugin;
    private CustomConfigManager customConfigManager;
    private API api;

    public DebugCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
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
            api.permissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&6Please use tab complete."));
            return true;
        } else if (args.length == 1 && (args[0].equalsIgnoreCase("date"))) {
            String date = api.getTimeFormattedString();
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
        } else if (args[0].equalsIgnoreCase("convert")) {
            if (args.length == 3 && args[2].equalsIgnoreCase("homes")) {
                DataTranslator dataTranslator = new DataTranslator();
                Map<UUID, Map<String, Location>> homesFrom = dataTranslator.convertHomesFrom(Software.valueOf(args[1]));
                Instant start = Instant.now();
                homesFrom.forEach(((uuid, map) -> map.forEach((homeName, location) -> this.plugin.getHomeManager().save(uuid, this.plugin.getServer().getOfflinePlayer(uuid).getName(), homeName, location))));
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toMillis();
                p.sendMessage(Translate.chat("&6Home data has been transferred took " + timeElapsed + "Ms"));
            }
        }
        return true;
    }
}