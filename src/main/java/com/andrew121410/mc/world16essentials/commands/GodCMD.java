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

public class GodCMD implements CommandExecutor {

    //Lists
    private final List<String> godm;
    //...

    private final World16Essentials plugin;
    private final API api;

    public GodCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.godm = this.plugin.getSetListMap().getGodList();

        this.plugin.getCommand("god").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.god")) {
            api.sendPermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            doGod(p, Optional.empty());
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.god.other")) {
                api.sendPermissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                doGod(target, Optional.of(p));
            }
            return true;
        } else {
            p.sendMessage(Translate.chat("&cUsage:&9 /god &aOR &9/god <Player>"));
        }
        return true;
    }

    private void doGod(Player player, Optional<Player> optionalPlayer) {
        if (godm.contains(player.getDisplayName())) {
            godm.remove(player.getDisplayName());
            player.sendMessage(Translate.chat("&e{GOD MODE} &cHas been turned off."));
            optionalPlayer.ifPresent(player1 -> player1.sendMessage(Translate.chat("&e{GOD MODE} &cHas been turned off to &9" + player.getDisplayName() + ".")));
        } else if (!godm.contains(player.getDisplayName())) {
            godm.add(player.getDisplayName());
            player.sendMessage(Translate.chat("&e{GOD MODE} &aHas been turned on."));
            optionalPlayer.ifPresent(player1 -> player1.sendMessage(Translate.chat("&e{GOD MODE} &aHas been turned on to &9" + player.getDisplayName() + ".")));
        }
    }
}