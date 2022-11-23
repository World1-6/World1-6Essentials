package com.andrew121410.mc.world16essentials.commands.kit;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class KitCMD implements CommandExecutor {

    private final Map<String, KitObject> kitsMap;

    private final World16Essentials plugin;
    private final API api;

    public KitCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();
        this.kitsMap = this.plugin.getSetListMap().getKitsMap();

        this.plugin.getCommand("kit").setExecutor(this);
        this.plugin.getCommand("kit").setTabCompleter((sender, command, alias, args) -> {
            if (!(sender instanceof Player player)) return null;
            if (!player.hasPermission("world16.kit.use")) return null;

            List<String> kits = new java.util.ArrayList<>(this.plugin.getSetListMap().getKitsMap().keySet().stream().toList());

            // If the player doesn't have the permission to use such kit, don't suggest it.
            kits.removeIf(kit -> !player.hasPermission("world16.kit.use." + kit));

            if (args.length == 1) {
                return TabUtils.getContainsString(args[0], kits);
            }
            return null;
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only Players Can Use This Command.");
                return true;
            }

            if (!player.hasPermission("world16.kit.use")) {
                api.sendPermissionErrorMessage(player);
                return true;
            }

            String name = args[0];
            giveKit(player, null, name);
            return true;
        } else if (args.length == 2) { // /kit <player> <kit>
            if (!sender.hasPermission("world16.kit.admin")) {
                api.sendPermissionErrorMessage(sender);
                return true;
            }

            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(Translate.color("&cThat player is not online!"));
                return true;
            }

            String name = args[1];

            giveKit(target, sender, name);
            return true;
        } else {
            sender.sendMessage(Translate.miniMessage("<red>/kit <name>"));
            sender.sendMessage(Translate.miniMessage("<red>/kit <player> <name>"));
        }
        return false;
    }

    private void giveKit(Player target, CommandSender sender, String kitName) {
        KitObject kitObject = this.kitsMap.getOrDefault(kitName, null);
        if (kitObject == null) {
            if (sender == null) {
                target.sendMessage(Translate.miniMessage("<red>That kit doesn't exist!"));
            } else {
                sender.sendMessage(Translate.miniMessage("<red>That kit doesn't exist!"));
            }
            return;
        }

        String permission = kitObject.getSettings().getPermission();
        if (!permission.equalsIgnoreCase("none") && !target.hasPermission(permission) && sender == null) {
            target.sendMessage(Translate.miniMessage("<red>You don't have permission to use this kit!"));
            return;
        }

        if (!target.isOp() && !this.plugin.getKitSettingsManager().handleCooldown(target, kitObject) && sender == null) {
            target.sendMessage(Translate.miniMessage("<red>You can't use this kit yet!"));
            target.sendMessage(Translate.miniMessage("<gold>You can use this kit in " + this.plugin.getKitSettingsManager().getTimeUntilCanUseAgain(target, kitObject)));
            return;
        }

        this.plugin.getKitManager().giveKit(target, kitObject);
        target.sendMessage(Translate.miniMessage("<green>You have received the kit: <blue>" + kitName));
    }
}
