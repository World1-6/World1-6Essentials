package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShouldKeepSpawnChunksLoadedCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public ShouldKeepSpawnChunksLoadedCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("shouldkeepspawnchunksloaded").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("world16.shouldkeepspawnchunksloaded")) {
            this.api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1) {
            String string = args[0];
            if (string.equalsIgnoreCase("true")) {
                player.getWorld().setKeepSpawnInMemory(true);
                this.plugin.getCustomConfigManager().getShitYml().getConfig().set("Worlds." + player.getWorld().getName() + ".ShouldKeepSpawnInMemory", "true");
                this.plugin.getCustomConfigManager().getShitYml().saveConfig();
                player.sendMessage(Translate.color("&eSpawn chunks will NOW be kept in memory EVEN if nobody is in them."));
                return true;
            } else if (string.equalsIgnoreCase("false")) {
                player.getWorld().setKeepSpawnInMemory(false);
                this.plugin.getCustomConfigManager().getShitYml().getConfig().set("Worlds." + player.getWorld().getName() + ".ShouldKeepSpawnInMemory", "false");
                this.plugin.getCustomConfigManager().getShitYml().saveConfig();
                player.sendMessage(Translate.color("&eSpawn chunks will NOT stay loaded if nobody is in them now."));
                return true;
            }
        }
        player.sendMessage(Translate.color("&9Current keepSpawnInMemory value = " + player.getWorld().getKeepSpawnInMemory()));
        player.sendMessage(Translate.color("&cUsage: &6/shouldkeepspawnchunksloaded &e<true/false>"));
        return true;
    }
}