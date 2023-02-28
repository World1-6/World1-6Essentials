package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ColorsCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public ColorsCMD(World16Essentials getPlugin) {
        this.plugin = getPlugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("colors").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("world16.colors")) {
            api.sendPermissionErrorMessage(sender);
            return true;
        }

        sender.sendMessage(Translate.miniMessage("<#00973B>World1-6Essentials now uses <red>MiniMessage<#00973B> for colors!"));
        sender.sendMessage(Translate.miniMessage("<blue>https://docs.advntr.dev/minimessage/format.html").clickEvent(ClickEvent.openUrl("https://docs.advntr.dev/minimessage/format.html")));
        sender.sendMessage(Translate.miniMessage("<#00973B>Here are some examples:"));
        sender.sendMessage("");
        sender.sendMessage(Translate.miniMessage("<rainbow>Colors:"));
        sender.sendMessage(Component.text("<black> -> ").color(NamedTextColor.BLACK).append(Translate.miniMessage("<black>Black")));
        sender.sendMessage(Component.text("<dark_blue> -> ").color(NamedTextColor.DARK_BLUE).append(Translate.miniMessage("<dark_blue>Dark Blue")));
        sender.sendMessage(Component.text("<dark_green> -> ").color(NamedTextColor.DARK_GREEN).append(Translate.miniMessage("<dark_green>Dark Green")));
        sender.sendMessage(Component.text("<dark_aqua> -> ").color(NamedTextColor.DARK_AQUA).append(Translate.miniMessage("<dark_aqua>Dark Aqua")));
        sender.sendMessage(Component.text("<dark_red> -> ").color(NamedTextColor.DARK_RED).append(Translate.miniMessage("<dark_red>Dark Red")));
        sender.sendMessage(Component.text("<dark_purple> -> ").color(NamedTextColor.DARK_PURPLE).append(Translate.miniMessage("<dark_purple>Dark Purple")));
        sender.sendMessage(Component.text("<gold> -> ").color(NamedTextColor.GOLD).append(Translate.miniMessage("<gold>Gold")));
        sender.sendMessage(Component.text("<gray> -> ").color(NamedTextColor.GRAY).append(Translate.miniMessage("<gray>Gray")));
        sender.sendMessage(Component.text("<dark_gray> -> ").color(NamedTextColor.DARK_GRAY).append(Translate.miniMessage("<dark_gray>Dark Gray")));
        sender.sendMessage(Component.text("<blue> -> ").color(NamedTextColor.BLUE).append(Translate.miniMessage("<blue>Blue")));
        sender.sendMessage(Component.text("<green> -> ").color(NamedTextColor.GREEN).append(Translate.miniMessage("<green>Green")));
        sender.sendMessage(Component.text("<aqua> -> ").color(NamedTextColor.AQUA).append(Translate.miniMessage("<aqua>Aqua")));
        sender.sendMessage(Component.text("<red> -> ").color(NamedTextColor.RED).append(Translate.miniMessage("<red>Red")));
        sender.sendMessage(Component.text("<light_purple> -> ").color(NamedTextColor.LIGHT_PURPLE).append(Translate.miniMessage("<light_purple>Light Purple")));
        sender.sendMessage(Component.text("<yellow> -> ").color(NamedTextColor.YELLOW).append(Translate.miniMessage("<yellow>Yellow")));
        sender.sendMessage(Component.text("<white> -> ").color(NamedTextColor.WHITE).append(Translate.miniMessage("<white>White")));
        sender.sendMessage("");
        sender.sendMessage(Translate.miniMessage("<#EA12E1>You can also use hex colors! <blue>(Example below)"));
        sender.sendMessage(Component.text("<#00973b> -> ").color(TextColor.color(0, 151, 59)).append(Translate.miniMessage("<#00973b>Hex Color")));
        return true;
    }
}
