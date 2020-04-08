package com.andrew121410.World16.Utils;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.MoneyManager;
import com.andrew121410.World16.Objects.MoneyObject;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VaultCore implements Economy {

    //So i can remember.
    //http://milkbowl.github.io/VaultAPI/

    private Map<UUID, MoneyObject> moneyMap;

    private Main plugin;
    private MoneyManager moneyManager;

    public VaultCore(Main plugin) {
        this.plugin = plugin;
        this.moneyManager = this.plugin.getMoneyManager();
        this.moneyMap = this.plugin.getSetListMap().getMoneyMap();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "World1-6Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return "$" + (long) amount;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String uuid) {
        return moneyManager.isUserConfig(UUID.fromString(uuid));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return moneyManager.isUserConfig(offlinePlayer.getUniqueId());
    }

    @Override
    public boolean hasAccount(String uuid, String world) {
        return hasAccount(uuid);
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
        return hasAccount(offlinePlayer);
    }

    @Override
    public double getBalance(String uuid) {
        return moneyManager.get(UUID.fromString(uuid)) ? moneyMap.get(UUID.fromString(uuid)).getBalanceExact() : 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return moneyManager.get(offlinePlayer.getUniqueId()) ? moneyMap.get(offlinePlayer.getUniqueId()).getBalanceExact() : 0;
    }

    @Override
    public double getBalance(String uuid, String worldName) {
        return getBalance(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String uuid, double amount) {
        UUID realuuid = UUID.fromString(uuid);
        Player target = Bukkit.getPlayer(realuuid);

        if (target != null) {
            if (moneyManager.isUser(UUID.fromString(uuid))) {
                return moneyMap.get(realuuid).hasEnough((long) amount);
            }
        }
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        if (moneyManager.isUser(offlinePlayer.getUniqueId())) {
            return moneyMap.get(offlinePlayer.getUniqueId()).hasEnough((long) amount);
        }
        return false;
    }

    @Override
    public boolean has(String uuid, String worldName, double amount) {
        return has(uuid, amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return has(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String uuid, double amount) {
        Player p = Bukkit.getPlayer(UUID.fromString(uuid));
        if (p != null) {
            if (moneyManager.isUser(UUID.fromString(uuid))) {
                if (this.moneyMap.get(UUID.fromString(uuid)).hasEnough((long) amount)) {
                    this.moneyMap.get(UUID.fromString(uuid)).subtractBalance((long) amount);
                    p.sendMessage(Translate.chat("&e$" + (long) amount + " &ahas been taken from your account."));
                    return new EconomyResponse(amount, this.moneyMap.get(UUID.fromString(uuid)).getBalanceExact(), EconomyResponse.ResponseType.SUCCESS, "You paid $" + amount);
                } else {
                    p.sendMessage(Translate.chat("You do not have enough money dumper."));
                    return new EconomyResponse(amount, this.moneyMap.get(UUID.fromString(uuid)).getBalanceExact(), EconomyResponse.ResponseType.FAILURE, "You do not have enough money!");
                }
            } else {
                p.sendMessage(Translate.chat("You do not have an account?"));
                return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "You do not have an account!");
            }
        }
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Not a valid player?");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
        if (moneyManager.isUser(offlinePlayer.getUniqueId())) {

            if (this.moneyMap.get(offlinePlayer.getUniqueId()).hasEnough((long) amount)) {
                this.moneyMap.get(offlinePlayer.getUniqueId()).subtractBalance((long) amount);
                return new EconomyResponse(amount, this.moneyMap.get(offlinePlayer.getUniqueId()).getBalanceExact(), EconomyResponse.ResponseType.SUCCESS, "You paid $" + amount);
            } else {
                return new EconomyResponse(amount, this.moneyMap.get(offlinePlayer.getUniqueId()).getBalanceExact(), EconomyResponse.ResponseType.FAILURE, "You do not have enough money!");
            }
        }
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "You do not have an account!");
    }

    @Override
    public EconomyResponse withdrawPlayer(String uuid, String worldName, double amount) {
        return withdrawPlayer(uuid, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String WorldName, double amount) {
        return withdrawPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String uuid, double amount) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));

        if (player != null) {
            if (moneyManager.isUser(UUID.fromString(uuid))) {
                moneyMap.get(UUID.fromString(uuid)).addBalance((long) amount);
                player.sendMessage(Translate.chat("&a$" + (long) amount + " has been added to your account."));
                return new EconomyResponse(amount, moneyMap.get(UUID.fromString(uuid)).getBalanceExact(), EconomyResponse.ResponseType.SUCCESS, "You have been paid $" + amount);
            } else {
                return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have an account!");
            }
        } else {
            return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Not a player?");
        }

    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
        if (moneyManager.isUser(offlinePlayer.getUniqueId())) {
            moneyMap.get(offlinePlayer.getUniqueId()).addBalance((long) amount);
            return new EconomyResponse(amount, moneyMap.get(offlinePlayer.getUniqueId()).getBalanceExact(), EconomyResponse.ResponseType.SUCCESS, "You have been paid $" + amount);
        }
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Player does not have an account!");
    }

    @Override
    public EconomyResponse depositPlayer(String uuid, String worldName, double amount) {
        return depositPlayer(uuid, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
        return depositPlayer(offlinePlayer, amount);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String uuid) {
        if (!hasAccount(uuid)) {
            moneyManager.get(UUID.fromString(uuid));
            return true;
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        if (!hasAccount(String.valueOf(offlinePlayer.getUniqueId()))) {
            moneyManager.get(offlinePlayer.getUniqueId());
            return true;
        }
        return false;
    }

    @Override
    public boolean createPlayerAccount(String uuid, String worldName) {
        return createPlayerAccount(uuid);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
        return createPlayerAccount(offlinePlayer);
    }
}
