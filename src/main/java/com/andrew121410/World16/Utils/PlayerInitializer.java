package com.andrew121410.World16.Utils;

import com.andrew121410.CCUtils.storage.ISQL;
import com.andrew121410.CCUtils.storage.SQLite;
import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16.Managers.KeyManager;
import com.andrew121410.World16.Objects.AfkObject;
import com.andrew121410.World16.Objects.KeyObject;
import com.andrew121410.World16.Objects.LocationObject;
import com.andrew121410.World16.Objects.PowerToolObject;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerInitializer {

    private Map<String, KeyObject> keyDataM;
    private Map<UUID, LocationObject> backM;
    private Map<UUID, PowerToolObject> powerToolMap;
    private Map<UUID, AfkObject> afkObjectMap;

    private List<Player> adminListPlayer;

    private ISQL isqlKeys;
    private KeyManager keyapi;

    private Main plugin;

    public PlayerInitializer(Main plugin) {
        this.plugin = plugin;

        this.keyDataM = this.plugin.getSetListMap().getKeyDataM();
        this.backM = this.plugin.getSetListMap().getBackM();
        this.adminListPlayer = this.plugin.getSetListMap().getAdminListPlayer();
        this.powerToolMap = this.plugin.getSetListMap().getPowerToolMap();
        this.afkObjectMap = this.plugin.getSetListMap().getAfkMap();

        //ISQL
        this.isqlKeys = new SQLite(this.plugin.getDataFolder(), "keys");
        //...

        this.keyapi = new KeyManager(this.plugin, this.isqlKeys);
    }

    public void load(Player player) {
        keyDataM.put(player.getDisplayName(), new KeyObject(player.getDisplayName(), 1, "null"));
        keyapi.getAllKeysISQL(player.getDisplayName(), isqlKeys);
        backM.put(player.getUniqueId(), new LocationObject());
        powerToolMap.put(player.getUniqueId(), new PowerToolObject());

        this.plugin.getHomeManager().load(player);
        this.plugin.getMoneyManager().load(player);
        this.afkObjectMap.put(player.getUniqueId(), new AfkObject(player));

        adminListPlayer.forEach((k) -> {
            player.hidePlayer(k);
            k.sendMessage(Translate.chat("[&9World1-6&r] &9Player: " + player.getDisplayName() + " &cnow cannot see you,"));
        });
    }

    public void unload(Player player) {
        this.plugin.getMoneyManager().save(player);

        this.plugin.getSetListMap().clearSetListMap(player);
    }
}
