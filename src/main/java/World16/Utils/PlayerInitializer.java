package World16.Utils;

import CCUtils.Storage.ISQL;
import CCUtils.Storage.SQLite;
import World16.Main.Main;
import World16.Managers.HomeManager;
import World16.Managers.KeyManager;
import World16.Objects.KeyObject;
import World16.Objects.LocationObject;
import World16.Objects.PowerToolObject;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerInitializer {

    private Map<String, KeyObject> keyDataM;
    private Map<UUID, LocationObject> backM;
    private Map<UUID, PowerToolObject> powerToolMap;

    private List<Player> adminListPlayer;

    private ISQL isqlKeys;
    private ISQL isqlHomes;

    private KeyManager keyapi;
    private HomeManager homeManager;

    private Main plugin;

    public PlayerInitializer(Main plugin) {
        this.plugin = plugin;

        this.keyDataM = this.plugin.getSetListMap().getKeyDataM();
        this.backM = this.plugin.getSetListMap().getBackM();
        this.adminListPlayer = this.plugin.getSetListMap().getAdminListPlayer();
        this.powerToolMap = this.plugin.getSetListMap().getPowerToolMap();

        //ISQL
        this.isqlKeys = new SQLite(this.plugin.getDataFolder(), "keys");
        this.isqlHomes = new SQLite(this.plugin.getDataFolder(), "Homes");
        //...

        this.keyapi = new KeyManager(this.plugin, this.isqlKeys);
        this.homeManager = new HomeManager(this.plugin, this.isqlHomes);
    }

    public void load(Player player) {
        keyDataM.put(player.getDisplayName(), new KeyObject(player.getDisplayName(), 1, "null"));
        keyapi.getAllKeysISQL(player.getDisplayName(), isqlKeys);
        backM.put(player.getUniqueId(), new LocationObject());
        powerToolMap.put(player.getUniqueId(), new PowerToolObject());

        this.homeManager.getAllHomesFromISQL(this.isqlHomes, player);

        adminListPlayer.forEach((k) -> {
            player.hidePlayer(k);
            k.sendMessage(Translate.chat("[&9World1-6&r] &9Player: " + player.getDisplayName() + " &cnow cannot see you,"));
        });
    }

    public void unload(Player player) {
        this.plugin.getSetListMap().clearSetListMap(player);
    }
}
