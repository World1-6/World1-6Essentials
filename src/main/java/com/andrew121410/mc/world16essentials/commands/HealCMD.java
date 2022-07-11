package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class HealCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public HealCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("heal").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("world16.heal")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            doHeal(player, null);
            return true;
        } else if (args.length == 1) {
            if (!player.hasPermission("world16.heal.other")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }
            Player target = plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                doHeal(target, player);
            }
            return true;
        } else {
            player.sendMessage(Translate.chat("&cUsage: for yourself do /heal OR /heal <Player>"));
        }
        return true;
    }

    private void doHeal(Player player, Player healer) {
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        for (PotionEffect effect : player.getActivePotionEffects()) player.removePotionEffect(effect.getType());

        player.sendMessage(Translate.chat("&6You have been healed."));
        if (healer != null) {
            healer.sendMessage(Translate.chat("&6You have healed &7" + player.getName() + "&6."));
        }
    }
}
