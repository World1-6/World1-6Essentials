package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.tabcomplete.BackTab;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.xutils.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class BackCMD implements CommandExecutor {

    private Map<UUID, Map<String, Location>> backMap;

    private Main plugin;
    private API api;

    public BackCMD(Main plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.backMap = this.plugin.getSetListMap().getBackMap();

        this.plugin.getCommand("back").setExecutor(this);
        this.plugin.getCommand("back").setTabCompleter(new BackTab(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player player = (Player) sender;

        Map<String, Location> playerBackMap = this.backMap.get(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(Translate.chat("[&cBack&r] &a&oHere's all of the back commands/sub."));
            player.sendMessage(Translate.chat("&6/back death"));
            player.sendMessage(Translate.chat("&6/back tp"));
            return true;
        }

        if (args[0].equalsIgnoreCase("death")) {
            if (!player.hasPermission("world16.back.death")) {
                api.PermissionErrorMessage(player);
                return true;
            }
            Location deathLocation = playerBackMap.get("Death");
            if (deathLocation == null) {
                player.sendMessage(Translate.chat("&4No death back location was found..."));
                return true;
            }

            //Checks if it's Lava Or Water.
            if (deathLocation.getBlock().isLiquid() || deathLocation.getBlock().getRelative(BlockFace.DOWN).isLiquid()) {
                deathLocation.getBlock().getRelative(BlockFace.DOWN).setType(XMaterial.OAK_LOG.parseMaterial());
                deathLocation.getBlock().getRelative(BlockFace.EAST).setType(XMaterial.OAK_LOG.parseMaterial());
                deathLocation.getBlock().getRelative(BlockFace.NORTH).setType(XMaterial.OAK_LOG.parseMaterial());
                deathLocation.getBlock().getRelative(BlockFace.WEST).setType(XMaterial.OAK_LOG.parseMaterial());
                deathLocation.getBlock().getRelative(BlockFace.SOUTH).setType(XMaterial.OAK_LOG.parseMaterial());
                deathLocation.getBlock().setType(Material.AIR);
            }

            player.teleport(deathLocation);
            player.sendMessage(Translate.chat("&6Teleporting..."));
        } else if (args[0].equalsIgnoreCase("tp")) {
            if (!player.hasPermission("world16.back.tp")) {
                api.PermissionErrorMessage(player);
                return true;
            }
            Location tpLocation = playerBackMap.get("Tp");
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