package com.andrew121410.mc.world16essentials.commands.tp;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class TpAcceptCMD implements CommandExecutor {

    private final Map<Player, Player> tpaMap;

    private final World16Essentials plugin;
    private final API api;

    private final CustomConfigManager customYmlManager;

    public TpAcceptCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customYmlManager = customConfigManager;
        this.api = this.plugin.getApi();

        this.tpaMap = this.plugin.getSetListMap().getTpaMap();

        this.plugin.getCommand("tpaccept").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("world16.tpaccept")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            Player tpa = this.tpaMap.get(player);
            if (tpa != null) {
                tpa.teleport(player);
                tpa.sendMessage(Translate.chat("[&eTPA&r] &a" + player.getDisplayName() + " has accepted your tpa request."));
                this.tpaMap.remove(player);
                return true;
            } else {
                player.sendMessage(Translate.chat("&e[TPA]&r &cLooks like you don't have any tpa request."));
            }
            return true;
        }
        return true;
    }
}