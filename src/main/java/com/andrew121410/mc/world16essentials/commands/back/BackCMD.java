package com.andrew121410.mc.world16essentials.commands.back;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class BackCMD implements CommandExecutor {

    private final Map<UUID, Map<BackEnum, Location>> backMap;

    private final World16Essentials plugin;
    private final API api;

    public BackCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.backMap = this.plugin.getSetListMap().getBackMap();

        this.plugin.getCommand("back").setExecutor(this);
        this.plugin.getCommand("back").setTabCompleter((sender, command, s, args) -> {
            if (!(sender instanceof Player)) return null;

            Player player = (Player) sender;

            if (!player.hasPermission("world16.back")) return null;

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], Arrays.asList("death", "tp"));
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

        Map<BackEnum, Location> playerBackMap = this.backMap.get(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(Translate.chat("[&cBack&r] &a&oHere's all of the back commands/sub."));
            player.sendMessage(Translate.chat("&6/back death"));
            player.sendMessage(Translate.chat("&6/back tp"));
            return true;
        }

        if (args[0].equalsIgnoreCase("death")) {
            if (!player.hasPermission("world16.back.death")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }
            Location deathLocation = playerBackMap.get(BackEnum.DEATH);
            if (deathLocation == null) {
                player.sendMessage(Translate.chat("&4No death back location was found..."));
                return true;
            }

            //Checks if it's Lava Or Water.
            if (deathLocation.getBlock().isLiquid() || deathLocation.getBlock().getRelative(BlockFace.DOWN).isLiquid()) {
                deathLocation.getBlock().getRelative(BlockFace.DOWN).setType(Material.LOG);
                deathLocation.getBlock().getRelative(BlockFace.EAST).setType(Material.LOG);
                deathLocation.getBlock().getRelative(BlockFace.NORTH).setType(Material.LOG);
                deathLocation.getBlock().getRelative(BlockFace.WEST).setType(Material.LOG);
                deathLocation.getBlock().getRelative(BlockFace.SOUTH).setType(Material.LOG);
                deathLocation.getBlock().setType(Material.AIR);
            }

            player.teleport(deathLocation);
            player.sendMessage(Translate.chat("&6Teleporting..."));
        } else if (args[0].equalsIgnoreCase("tp")) {
            if (!player.hasPermission("world16.back.tp")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }
            Location tpLocation = playerBackMap.get(BackEnum.TELEPORT);
            if (tpLocation == null) {
                player.sendMessage(Translate.chat("&4No tp back location was found..."));
                return true;
            }
            player.teleport(tpLocation);
            player.sendMessage(Translate.chat("&6Teleporting..."));
        }
        return true;
    }
}