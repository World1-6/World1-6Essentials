package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountAllEntitiesCMD implements CommandExecutor {
    private final World16Essentials plugin;
    private final API api;

    public CountAllEntitiesCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("countallentities").setExecutor(this);
        this.plugin.getCommand("countallentities").setTabCompleter((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.countallentities")) return null;

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], Arrays.stream(EntityType.values()).map(EntityType::name).toList());
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

        if (!player.hasPermission("world16.countallentities")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            List<Entity> allEntities = new ArrayList<>();
            for (World world : this.plugin.getServer().getWorlds()) {
                allEntities.addAll(world.getEntities());
            }

            player.sendMessage(Translate.color("&aThere are &e" + allEntities.size() + " &aentities on the server."));
        } else {
            EntityType entityType = null;
            try {
                entityType = EntityType.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }

            if (entityType == null) {
                player.sendMessage(Translate.color("&cInvalid Entity Type."));
                return true;
            }

            List<Entity> entities = new ArrayList<>();
            for (World world : this.plugin.getServer().getWorlds()) {
                Class<? extends Entity> clazz = entityType.getEntityClass();
                if (clazz == null) continue;

                entities.addAll(world.getEntitiesByClass(entityType.getEntityClass()));
            }

            player.sendMessage(Translate.color("&a&lTotal Entities: &f" + entities.size() + " &aof type &f" + entityType.name()));
        }
        return true;
    }
}