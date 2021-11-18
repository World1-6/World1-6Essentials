package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class FlyCMD implements CommandExecutor {

    //Lists
    private List<String> flyList;
    //....

    private World16Essentials plugin;
    private API api;

    public FlyCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.flyList = this.plugin.getSetListMap().getFlyList();

        this.plugin.getCommand("fly").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.fly")) {
            api.sendPermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            doFly(p, Optional.empty());
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.fly.other")) {
                api.sendPermissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                doFly(target, Optional.of(p));
            }
            return true;
        } else {
            p.sendMessage(Translate.chat("&cUsage: for yourself do /fly OR /fly <Player>"));
        }
        return true;
    }

    private void doFly(Player player, Optional<Player> optionalPlayer) {
        if (!flyList.contains(player.getDisplayName()) && (!player.isFlying())) {
            player.setAllowFlight(true);
            player.sendMessage(Translate.chat("&6Set fly mode &cenabled&6 for " + player.getDisplayName()));
            optionalPlayer.ifPresent(player1 -> player1.sendMessage(Translate.chat("&6Set fly mode &cenabled&6 for " + player.getDisplayName())));
            flyList.add(player.getDisplayName());
        } else if (flyList.contains(player.getDisplayName())) {
            player.setAllowFlight(false);
            player.sendMessage(Translate.chat("&6Set fly mode &cdisabled&6 for " + player.getDisplayName()));
            optionalPlayer.ifPresent(player1 -> player1.sendMessage(Translate.chat("&6Set fly mode &cdisabled&6 for " + player.getDisplayName())));
            flyList.remove(player.getDisplayName());
        }
    }
}