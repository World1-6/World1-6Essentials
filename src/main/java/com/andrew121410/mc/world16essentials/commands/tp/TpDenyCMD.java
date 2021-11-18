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

public class TpDenyCMD implements CommandExecutor {

    //Maps
    private Map<Player, Player> tpam;
    //...

    private World16Essentials plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public TpDenyCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = new API(this.plugin);

        this.tpam = this.plugin.getSetListMap().getTpaMap();

        this.plugin.getCommand("tpdeny").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.tpdeny")) {
            api.sendPermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            Player tpa = this.tpam.get(p);
            if (tpa != null) {
                p.sendMessage(Translate.chat("&9Ok you denied the tp request."));
                tpa.sendMessage(Translate.chat("[&eTPA&r] &cYour tpa request got denied by " + p.getDisplayName()));
                this.tpam.remove(p);
            } else {
                p.sendMessage(Translate.chat("&4Something went wrong."));
            }
        } else {
            p.sendMessage(Translate.chat("&4???"));
        }
        return true;
    }
}