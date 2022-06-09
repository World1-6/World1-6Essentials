package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CountAllEntities implements CommandExecutor {
    private final World16Essentials plugin;
    private final API api;

    public CountAllEntities(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("countallentities").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.countallentities")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        List<Entity> allEntities = new ArrayList<>();
        for (World world : this.plugin.getServer().getWorlds()) {
            allEntities.addAll(world.getEntities());
        }

        player.sendMessage("Count of all entities on all worlds: " + allEntities.size());
        player.sendMessage("Count of entities on current world: " + player.getWorld().getEntities().size());
        return true;
    }
}