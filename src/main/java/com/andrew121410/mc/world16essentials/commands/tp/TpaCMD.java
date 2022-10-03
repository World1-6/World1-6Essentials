package com.andrew121410.mc.world16essentials.commands.tp;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TpaCMD implements CommandExecutor {

    private final Map<UUID, UUID> tpaMap;

    private final World16Essentials plugin;
    private final API api;

    public TpaCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.tpaMap = this.plugin.getSetListMap().getTpaMap();

        this.plugin.getCommand("tpa").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.tpa")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1) {
            Player target = plugin.getServer().getPlayerExact(args[0]); //Get the player
            if (target != null && target.isOnline()) {
                tpaMap.put(target.getUniqueId(), player.getUniqueId());
                player.sendMessage(Translate.chat("[&eTPA&r] &9Sent tpa request too " + target.getDisplayName()));
                sendTpaRequestMessage(player, target);
            } else {
                player.sendMessage("&4Looks like that player is offline.");
            }
            return true;
        } else {
            player.sendMessage(Translate.miniMessage("<red>Usage: <gold>/tpa <player>"));
        }
        return true;
    }

    private void sendTpaRequestMessage(Player player, Player target) {
        if (player != null && target != null) {
            target.sendMessage(Translate.chat("[&eTPA&r] &a" + player.getDisplayName() + " has sent a tpa request too you."));
            target.sendMessage(Translate.chat("&c/tpaccept &aOR&r &c/tpdeny"));
        }
    }
}