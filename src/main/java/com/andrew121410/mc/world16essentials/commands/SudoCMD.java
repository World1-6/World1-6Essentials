package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudoCMD implements CommandExecutor, TabCompleter {

    private final World16Essentials plugin;
    private final API api;

    public SudoCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("sudo").setExecutor(this);
        this.plugin.getCommand("sudo").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.sudo")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Translate.miniMessage("<red>Usage: <gold>/sudo <player> <command> [--bypass-permissions]"));
            sender.sendMessage(Translate.miniMessage("<red>Example: <gold>/sudo Notch /back death --bypass-permissions"));
            return true;
        }

        boolean bypassPermissions = false;
        String lastArg = args[args.length - 1];

        // Check if the last argument is the bypass flag
        if (lastArg.equalsIgnoreCase("--bypass-permissions")) {
            bypassPermissions = true;
            args = Arrays.copyOfRange(args, 0, args.length - 1); // Remove the bypass flag
        }

        Player target = this.plugin.getServer().getPlayerExact(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(Translate.miniMessage("<dark_red>Player is not online!"));
            return true;
        }

        // Join the command arguments (everything after the player name)
        String command = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        // Remove the leading slash from the command, if present
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        if (bypassPermissions) {
            return executeBypass(target, sender, command);
        } else {
            return executeRegular(target, sender, command);
        }
    }

    private boolean executeRegular(Player target, CommandSender sender, String command) {
        try {
            this.plugin.getServer().dispatchCommand(target, command);
            sender.sendMessage(Translate.miniMessage("<dark_aqua>Command <gold>" + command + " <dark_aqua>has been executed as <gold>" + target.getName()));
            return true;
        } catch (Exception e) {
            sender.sendMessage(Translate.miniMessage("<dark_red>Failed to execute command: <gold>" + command));
            e.printStackTrace();
            return false;
        }
    }

    private boolean executeBypass(Player target, CommandSender sender, String command) {
        boolean wasOp = target.isOp();
        try {
            target.setOp(true);
            this.plugin.getServer().dispatchCommand(target, command);
            sender.sendMessage(Translate.miniMessage("<dark_aqua>Command <gold>" + command + " <dark_aqua>has been executed as <gold>" + target.getName() + " <dark_aqua>(with OP privileges)"));
            return true;
        } catch (Exception e) {
            sender.sendMessage(Translate.miniMessage("<dark_red>Failed to execute command: <gold>" + command));
            e.printStackTrace();
            return false;
        } finally {
            target.setOp(wasOp);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (!sender.hasPermission("world16.sudo")) {
            return null; // No suggestions for players without permission
        }

        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            // Suggest online player names for the first argument
            this.plugin.getServer().getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
        } else if (args.length > 2 && args[args.length - 1].isEmpty()) {
            // Suggest "--bypass-permissions" if no command is yet entered
            suggestions.add("--bypass-permissions");
        }
        return suggestions;
    }
}