package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeatsCMD implements CommandExecutor {

    private Main plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public SeatsCMD(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.customConfigManager = customConfigManager;

        this.plugin.getCommand("seats").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.seats")) {
            api.PermissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.chat("&cUsage: &6/seats <TRUEorFALSE &9[FALSE will turn off the seats for your Player.]"));
            return true;
        } else if (args.length == 1) {
            boolean enabled = api.asBooleanOrDefault(args[0], true);

            api.getPlayersYML(this.customConfigManager, p).set("seats", enabled);
            p.sendMessage(Translate.chat("Seats has been set to: " + enabled));
            return true;
        }
        return true;
    }
}
