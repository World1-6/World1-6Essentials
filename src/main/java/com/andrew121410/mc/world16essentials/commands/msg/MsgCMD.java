package com.andrew121410.mc.world16essentials.commands.msg;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MsgCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public MsgCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("emsg").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.msg") || !player.hasPermission("world16.emsg")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length >= 2) {
            Player target = this.plugin.getServer().getPlayerExact(args[0]);
            if (target != null && target.isOnline()) {
                String messageFrom = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                player.sendMessage(Translate.color("&2[&a{me} &6->&c {target}&2]&9 ->&r {message}").replace("{me}", "me").replace("{target}", target.getDisplayName()).replace("{message}", messageFrom));
                target.sendMessage(Translate.color("&2[&a{me} &6->&c {target}&2]&9 ->&r {message}").replace("{me}", player.getDisplayName()).replace("{target}", "me").replace("{message}", messageFrom));
                return true;
            }
        } else {
            player.sendMessage(Translate.colorc("&cUsage: /emsg <Player> <Message>"));
        }
        return true;
    }
}