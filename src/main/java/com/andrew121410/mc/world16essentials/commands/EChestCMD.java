package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EChestCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public EChestCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("echest").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.echest")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            openEChest(player, null);
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("world16.echest.other")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Translate.miniMessage("<red>Player is not online."));
                return true;
            }

            openEChest(target, sender);
            return true;
        }
        return true;
    }

    private void openEChest(Player target, CommandSender sender) {
        target.openInventory(target.getEnderChest());
        target.playSound(target.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 10.0f, 1.0f);
        target.sendMessage(Translate.miniMessage("<dark_purple>Opening Ender Chest..."));
        if (sender != null) {
            sender.sendMessage(Translate.miniMessage("<dark_purple>Opening <white>" + target.getName() + "'s <dark_purple>Ender Chest..."));
        }
    }
}
