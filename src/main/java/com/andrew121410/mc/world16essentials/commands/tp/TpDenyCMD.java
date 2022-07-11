package com.andrew121410.mc.world16essentials.commands.tp;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class TpDenyCMD implements CommandExecutor {

    private final Map<Player, Player> tpaMap;

    private final World16Essentials plugin;
    private final API api;

    public TpDenyCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.tpaMap = this.plugin.getSetListMap().getTpaMap();

        this.plugin.getCommand("tpdeny").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("world16.tpdeny")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            Player tpa = this.tpaMap.get(player);
            if (tpa != null) {
                player.sendMessage(Translate.chat("&9Ok you denied the tp request."));
                tpa.sendMessage(Translate.chat("[&eTPA&r] &cYour tpa request got denied by " + player.getDisplayName()));
                this.tpaMap.remove(player);
            } else {
                player.sendMessage(Translate.chat("&4Something went wrong."));
            }
        } else {
            player.sendMessage(Translate.chat("&4???"));
        }
        return true;
    }
}