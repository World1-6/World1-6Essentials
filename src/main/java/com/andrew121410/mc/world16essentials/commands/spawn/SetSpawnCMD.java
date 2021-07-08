package com.andrew121410.mc.world16essentials.commands.spawn;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.CustomYmlManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    private CustomYmlManager shitYml;

    public SetSpawnCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.shitYml = customConfigManager.getShitYml();
        this.api = this.plugin.getApi();

        this.plugin.getCommand("setspawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.setspawn")) {
            api.permissionErrorMessage(p);
            return true;
        }

        this.api.setLocationToFile(this.shitYml, "Spawn.default", p.getLocation());
        p.sendMessage(Translate.chat("&6Spawn location set for group default."));
        return true;
    }
}