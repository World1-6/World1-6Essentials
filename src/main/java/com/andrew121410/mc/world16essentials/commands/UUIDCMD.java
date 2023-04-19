package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UUIDCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public UUIDCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("uuid").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.uuid")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            Component component = Translate.miniMessage("<green>Your uuid is <reset>" + player.getUniqueId()).clickEvent(ClickEvent.copyToClipboard(String.valueOf(player.getUniqueId())));
            player.sendMessage(component);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.uuid.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Translate.colorc("&cThat player is not online."));
                return true;
            }

            Component component = Translate.miniMessage("<green>" + target.getName() + "'s uuid is <reset>" + target.getUniqueId()).clickEvent(ClickEvent.copyToClipboard(String.valueOf(target.getUniqueId())));
            sender.sendMessage(component);
            return true;
        } else {
            sender.sendMessage(Translate.colorc("&cUsage: /uuid or /uuid <player>"));
        }
        return true;
    }
}