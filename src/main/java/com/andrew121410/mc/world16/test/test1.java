package com.andrew121410.mc.world16.test;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.managers.CustomConfigManager;
import com.andrew121410.mc.world16.utils.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class test1 implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public test1(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.customConfigManager = customConfigManager;

        this.plugin.getCommand("testee1").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.testee1")) {
            api.permissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            //SOMETHING HERE
            return true;
        }
        return true;
    }
}