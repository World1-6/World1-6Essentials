package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class GodCMD implements CommandExecutor {

    private final List<UUID> godList;

    private final World16Essentials plugin;
    private final API api;

    public GodCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.godList = this.plugin.getSetListMap().getGodList();

        this.plugin.getCommand("god").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.god")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            doGod(player, null);
            return true;
        } else if (args.length == 1) {
            if (!player.hasPermission("world16.god.other")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(Translate.colorc("&cThat player is not online."));
                return true;
            }

            doGod(target, player);
            return true;
        } else {
            player.sendMessage(Translate.chat("&cUsage:&9 /god &aOR &9/god <Player>"));
        }
        return true;
    }

    private void doGod(Player target, Player sender) {
        String color = target.isOp() ? "&4" : "&7";
        if (!godList.contains(target.getUniqueId())) {
            godList.add(target.getUniqueId());
            target.sendMessage(Translate.colorc("&6Set god mode &cenabled &6for " + color + target.getName()));
            if (sender != null) {
                sender.sendMessage(Translate.colorc("&6Set god mode &cenabled &6for " + color + target.getName()));
            }
        } else if (godList.contains(target.getUniqueId())) {
            godList.remove(target.getUniqueId());
            target.sendMessage(Translate.colorc("&6Set god mode &cdisabled &6for " + color + target.getName()));
            if (sender != null) {
                sender.sendMessage(Translate.colorc("&6Set god mode &cdisabled &6for " + color + target.getName()));
            }
        }
    }
}