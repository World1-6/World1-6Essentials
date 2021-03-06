package com.andrew121410.mc.world16.commands;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CommandBlockFindCMD implements CommandExecutor {

    private List<String> spyCommandBlock;

    private World16Essentials plugin;
    private API api;

    public CommandBlockFindCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.spyCommandBlock = this.plugin.getSetListMap().getSpyCommandBlock();

        this.plugin.getCommand("commandblockfind").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("world16.commandblockfind")) {
            api.permissionErrorMessage(p);
            return true;
        }

        if (args.length >= 2) {
            String yesOrNo = args[0];
            String a = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            if (yesOrNo.equalsIgnoreCase("add")) {
                spyCommandBlock.add(a);
                p.sendMessage(Translate.chat("It has been added. A: " + a));
            } else if (yesOrNo.equalsIgnoreCase("remove") || yesOrNo.equalsIgnoreCase("delete")) {
                spyCommandBlock.remove(a);
                p.sendMessage(Translate.chat("It has been deleted. A: " + a));
            } else {
                p.sendMessage(Translate.chat("ADD OR REMOVE?"));
            }
        } else {
            p.sendMessage(Translate.chat("&cUsage: &6/commandblockfind add/remove <String>"));
        }
        return true;
    }
}