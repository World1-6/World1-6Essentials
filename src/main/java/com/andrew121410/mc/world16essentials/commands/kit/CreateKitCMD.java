package com.andrew121410.mc.world16essentials.commands.kit;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.BukkitSerialization;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateKitCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public CreateKitCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.plugin.getCommand("createkit").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.kit.create")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1) {
            String name = args[0];

            String[] data = BukkitSerialization.turnInventoryIntoBase64s(player);
            this.plugin.getKitManager().addKit(player, name, data[0], data[1]);
            player.sendMessage(Translate.miniMessage("<green>Kit <blue>" + name + " <green>has been created!"));
            return true;
        }
        player.sendMessage(Translate.miniMessage("<red>Usage: /createkit <name>"));
        return false;
    }
}
