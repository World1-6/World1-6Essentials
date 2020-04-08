package com.andrew121410.World16.Commands.economy;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.MoneyManager;
import com.andrew121410.World16.Objects.MoneyObject;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import com.andrew121410.World16.Utils.VaultCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class bal implements CommandExecutor {

    private Map<UUID, MoneyObject> moneyMap;

    private Main plugin;
    private API api;

    //Managers
    private MoneyManager dataManager;

    private VaultCore theCore;

    public bal(Main plugin) {
        this.plugin = plugin;
        this.moneyMap = this.plugin.getSetListMap().getMoneyMap();
        this.api = this.plugin.getApi();

        this.dataManager = this.plugin.getMoneyManager();

        this.theCore = this.plugin.getOtherPlugins().getVaultCore();

        this.plugin.getCommand("bal").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("world16.bal")) {
            p.sendMessage(Translate.chat("&cYou do not have permission to use this command."));
            return true;
        }

        if (args.length == 0) {
            if (moneyMap.get(p.getUniqueId()) != null) {
                p.sendMessage(Translate.chat("&aBalance:&c " + moneyMap.get(p.getUniqueId()).getBalanceFancy()));
                return true;
            } else {
                theCore.hasAccount(p.getUniqueId().toString());
                return true;
            }
        } else if (args.length == 1) {
            if (!p.hasPermission("world16.bal.other")) {
                p.sendMessage(Translate.chat("&cYou do not have permission to use this command."));
                return true;
            }
            String playerString = args[0];
            Player target = this.plugin.getServer().getPlayer(playerString);

            if (!targetChecker(p, target)) return true;

            p.sendMessage(Translate.chat("&aBalance of " + target.getDisplayName() + " is " + moneyMap.get(target.getUniqueId()).getBalanceFancy()));
        }
        return true;
    }

    private Boolean targetChecker(Player p, Player targetPlayer) {
        if (targetPlayer == null) {
            p.sendMessage(Translate.chat("I'm a 100% sure that isn't a player?"));
            return false;
        }

        if (!targetPlayer.isOnline()) {
            p.sendMessage(Translate.chat("That Player isn't online?"));
            return false;
        }

        if (!dataManager.isUserMap(targetPlayer.getUniqueId())) {
            p.sendMessage(Translate.chat("&cIf you see this report this to Andrew121410#2035 on discord&r"));
            p.sendMessage(this.getClass() + " " + "!dataManager.isUserMap" + " " + "LINE: 94");
            return false;
        }
        return true;
    }
}