package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SudoCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    public SudoCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("workbench").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length >= 2) {
                if (!sudoCommand(args)) {
                    sender.sendMessage("Player wasn't found.");
                } else sender.sendMessage("The command was ran successfully for the user.");
            }
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.sudo")) {
            api.permissionErrorMessage(p);
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(Translate.color("&6Usage:&r &c/sudo <Player> <Command>"));
            return true;
        } else if (args.length >= 2) {
            if (!sudoCommand(args)) p.sendMessage(Translate.color("&cPlayer wasn't found."));
            else p.sendMessage(Translate.color("&6[Sudo]&r &2The command was ran successfully for the user."));
        }
        return true;
    }

    private boolean sudoCommand(String[] args) {
        Player player = this.plugin.getServer().getPlayer(args[0]);
        if (player == null) return false;
        String[] commandArray = Arrays.copyOfRange(args, 1, args.length);
        if (commandArray[0].contains("/")) commandArray[0] = commandArray[0].replace("/", "");
        String command = String.join(" ", commandArray);
        this.plugin.getServer().dispatchCommand(player, command);
        return true;
    }
}
