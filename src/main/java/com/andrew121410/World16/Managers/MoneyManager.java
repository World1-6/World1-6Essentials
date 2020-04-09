package com.andrew121410.World16.Managers;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Objects.MoneyObject;
import com.andrew121410.World16.Utils.API;
import com.andrew121410.World16.Utils.Translate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class MoneyManager {

    private Map<UUID, MoneyObject> moneyMap;

    private Main plugin;
    private CustomYmlManager userConfig;

    public MoneyManager(Main plugin, CustomConfigManager customConfigManager) {
        this.plugin = plugin;
        this.userConfig = customConfigManager.getMoneyYml();
        this.moneyMap = this.plugin.getSetListMap().getMoneyMap();
    }

    public void load(Player player) {
        MoneyObject moneyObject = get(player.getUniqueId());
        this.moneyMap.putIfAbsent(player.getUniqueId(), moneyObject);
    }

    public void save(Player player) {
        save(player.getUniqueId(), moneyMap.get(player.getUniqueId()));
        this.moneyMap.remove(player.getUniqueId());
    }

    public MoneyObject get(UUID uuid) {
        ConfigurationSection cs = this.userConfig.getConfig().getConfigurationSection(uuid.toString());

        //Create new User.
        if (cs == null) {
            this.plugin.getServer().getConsoleSender().sendMessage(Translate.chat(API.USELESS_TAG + " " + "New User: " + uuid.toString()));
            cs = this.userConfig.getConfig().createSection(uuid.toString());
            MoneyObject userObject = new MoneyObject(uuid, API.DEFAULT_MONEY);
            cs.set("MoneyObject", userObject);
            return userObject;
        }

        return (MoneyObject) cs.get("MoneyObject");
    }

    public void save(UUID uuid, MoneyObject moneyObject) {
        ConfigurationSection cs = this.userConfig.getConfig().getConfigurationSection(uuid.toString());
        if (cs == null) cs = this.userConfig.getConfig().createSection(uuid.toString());
        cs.set("MoneyObject", moneyObject);
        this.userConfig.saveConfig();
    }


    public boolean isUser(UUID uuid) {
        return isUserConfig(uuid) && isUserMap(uuid);
    }

    public boolean isUserConfig(UUID uuid) {
        ConfigurationSection cs = this.userConfig.getConfig().getConfigurationSection(uuid.toString());
        return cs != null;
    }

    public boolean isUserMap(UUID uuid) {
        return moneyMap.get(uuid) != null;
    }
}