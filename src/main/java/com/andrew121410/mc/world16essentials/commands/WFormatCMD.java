package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.managers.CustomConfigManager;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class WFormatCMD implements CommandExecutor {

    private World16Essentials plugin;
    private API api;

    private CustomConfigManager customConfigManager;

    public WFormatCMD(World16Essentials plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.customConfigManager = customConfigManager;
        this.api = new API(this.plugin);

        this.plugin.getCommand("wformat").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {

            if (!(sender instanceof BlockCommandSender)) {
                return true;
            }

            BlockCommandSender cmdblock = (BlockCommandSender) sender;
            Block commandblock = cmdblock.getBlock();

            if (args.length == 0) {
                sender.sendMessage("Wtf? what you mean?");
                return true;
            } else if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("cmd")) {
                    if (args[1] == null) {
                        sender.sendMessage(Translate.chat("&cUsage: /wformat cmd <String>"));
                        return true;
                    }
                    String[] commandRaw = Arrays.copyOfRange(args, 1, args.length);

                    if (commandRaw[0].contains("/")) {
                        commandRaw[0] = commandRaw[0].replace("/", "");
                    }

                    int z = 0;

                    for (int i = 0; i < commandRaw.length; i++) {
                        if (commandRaw[i].contains("~")) {
                            commandRaw[i] = commandRaw[i].replace("~", "");
                            switch (z) {
                                case 0:
                                    commandRaw[i] = "X~" + commandRaw[i];
                                    z++;
                                    break;
                                case 1:
                                    commandRaw[i] = "Y~" + commandRaw[i];
                                    z++;
                                    break;
                                case 2:
                                    commandRaw[i] = "Z~" + commandRaw[i];
                                    z++;
                                    break;
                                default:
                                    z = 1;
                                    commandRaw[i] = "X~" + commandRaw[i];
                                    break;
                            }
                        }
                    }

                    String command = String.join(API.CUSTOM_COMMAND_FORMAT, commandRaw);
                    sender.sendMessage(command);
                    return true;
                } else if (args[0].equalsIgnoreCase("uncmd")) {
                    if (args.length < 2) {
                        sender.sendMessage(Translate.chat("&cUsage: /wformat uncmd <String>"));
                        return true;
                    }
                    String[] commandRaw = Arrays.copyOfRange(args, 1, args.length);

                    String str1 = Arrays.toString(commandRaw);
                    str1 = str1.substring(1, str1.length() - 1);
                    commandRaw = str1.split(API.CUSTOM_COMMAND_FORMAT);

                    for (int i = 0; i < commandRaw.length; i++) {
                        if (commandRaw[i].contains("~")) {
                            String letter = commandRaw[i].substring(0, 1);
                            commandRaw[i] = commandRaw[i].replace(letter, "");
                        }
                    }

                    String command = String.join(" ", commandRaw);

                    sender.sendMessage(command);
                    return true;
                }
                return true;
            }
            return true;
        }


        Player p = (Player) sender;
        if (!p.hasPermission("world16.wformat")) {
            api.permissionErrorMessage(p);
            return true;
        }
        if (args.length == 0) {
            p.sendMessage("Wtf? what you mean?");
            return true;
        } else {
            if (args[0].equalsIgnoreCase("cmd")) {
                if (args.length < 2) {
                    p.sendMessage(Translate.chat("&cUsage: /wformat cmd <String>"));
                    return true;
                }

                String[] commandRaw = Arrays.copyOfRange(args, 1, args.length);

                if (commandRaw[0].contains("/")) {
                    commandRaw[0] = commandRaw[0].replace("/", "");
                }

                int z = 0;

                for (int i = 0; i < commandRaw.length; i++) {
                    if (commandRaw[i].contains("~")) {
                        commandRaw[i] = commandRaw[i].replace("~", "");
                        switch (z) {
                            case 0:
                                commandRaw[i] = "X~" + commandRaw[i];
                                z++;
                                break;
                            case 1:
                                commandRaw[i] = "Y~" + commandRaw[i];
                                z++;
                                break;
                            case 2:
                                commandRaw[i] = "Z~" + commandRaw[i];
                                z++;
                                break;
                            default:
                                z = 1;
                                commandRaw[i] = "X~" + commandRaw[i];
                                break;
                        }
                        p.sendMessage(Translate.chat("&cDebug -> " + commandRaw[i] + " =Z= " + z));
                    }
                }

                String command = String.join(API.CUSTOM_COMMAND_FORMAT, commandRaw);

                BaseComponent[] components = new ComponentBuilder("[CMD] Now Click Me").event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)).create();
                p.spigot().sendMessage(components);
                return true;
            } else if (args[0].equalsIgnoreCase("uncmd")) {
                if (args.length < 2) {
                    p.sendMessage(Translate.chat("&cUsage: /wformat uncmd <String>"));
                    return true;
                }
                String[] commandRaw = Arrays.copyOfRange(args, 1, args.length);

                String str1 = Arrays.toString(commandRaw);
                str1 = str1.substring(1, str1.length() - 1);
                commandRaw = str1.split(API.CUSTOM_COMMAND_FORMAT);

                for (int i = 0; i < commandRaw.length; i++) {
                    if (commandRaw[i].contains("~")) {
                        String letter = commandRaw[i].substring(0, 1);
                        commandRaw[i] = commandRaw[i].replace(letter, "");
                    }
                }

                String command = String.join(" ", commandRaw);

                BaseComponent[] components = new ComponentBuilder("[UNCMD] Now Click Me").event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)).create();
                p.spigot().sendMessage(components);
                return true;
            }
            return true;
        }
    }
}