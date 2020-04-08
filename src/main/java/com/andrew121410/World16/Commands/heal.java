package com.andrew121410.World16.Commands;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Optional;

public class heal implements CommandExecutor {

    private Main plugin;
    private API api;

    public heal(Main plugin) {
        this.plugin = plugin;
        this.api = new API(this.plugin);

        this.plugin.getCommand("heal").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.heal")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            doHeal(p, Optional.empty());
            return true;
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.heal.other")) {
                api.PermissionErrorMessage(p);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                doHeal(target, Optional.of(p));
            }
            return true;
        } else {
            p.sendMessage(Translate.chat("&cUsage: for yourself do /heal OR /heal <Player>"));
        }
        return true;
    }

    private void doHeal(Player player, Optional<Player> optionalPlayer) {
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        for (PotionEffect effect : player.getActivePotionEffects()) player.removePotionEffect(effect.getType());

        player.sendMessage(Translate.chat("&6You have been healed."));
        optionalPlayer.ifPresent(player1 -> player1.sendMessage(Translate.chat("&6You just healed " + player.getDisplayName())));
    }
}
