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
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.kit.use")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1) {
            String name = args[0];

            KitObject kitObject = this.kitsMap.getOrDefault(name, null);
            if (kitObject == null) {
                player.sendMessage(Translate.miniMessage("<red>That kit doesn't exist!"));
                return true;
            }

            String permission = kitObject.getSettings().getPermission();
            if (!permission.equalsIgnoreCase("none") && !player.hasPermission(permission)) {
                player.sendMessage(Translate.miniMessage("<red>You don't have permission to use this kit!"));
                return true;
            }

            if (!player.isOp() && !this.plugin.getKitSettingsManager().handleCooldown(player, kitObject)) {
                player.sendMessage(Translate.miniMessage("<red>You can't use this kit yet!"));
                player.sendMessage(Translate.miniMessage("<gold>You can use this kit in " + this.plugin.getKitSettingsManager().getTimeUntilCanUseAgain(player, kitObject)));
                return true;
            }
            this.plugin.getKitManager().giveKit(player, kitObject);
            player.sendMessage(Translate.miniMessage("<green>You have received the kit: <blue>" + name));
            return true;
        }
        player.sendMessage(Translate.miniMessage("<red>Usage: /kit <name>"));
        return false;
    }
}
