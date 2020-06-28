package com.andrew121410.mc.world16.utils;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.objects.AfkObject;
import com.andrew121410.mc.world16.objects.PowerToolObject;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerInitializer {

    private Map<UUID, Map<String, Location>> backMap;
    private Map<UUID, PowerToolObject> powerToolMap;
    private Map<UUID, AfkObject> afkObjectMap;

    private List<Player> hiddenPlayersList;

    private Main plugin;

    public PlayerInitializer(Main plugin) {
        this.plugin = plugin;

        this.backMap = this.plugin.getSetListMap().getBackMap();
        this.hiddenPlayersList = this.plugin.getSetListMap().getHiddenPlayers();
        this.powerToolMap = this.plugin.getSetListMap().getPowerToolMap();
        this.afkObjectMap = this.plugin.getSetListMap().getAfkMap();
    }

    public void load(Player player) {
        backMap.put(player.getUniqueId(), new HashMap<>());
        powerToolMap.put(player.getUniqueId(), new PowerToolObject());

        this.plugin.getHomeManager().load(player);
        this.afkObjectMap.put(player.getUniqueId(), new AfkObject(player));

        hiddenPlayersList.forEach((k) -> {
            player.hidePlayer(k);
            k.sendMessage(Translate.chat("[&9World1-6&r] &9Player: " + player.getDisplayName() + " &cnow cannot see you,"));
        });
    }

    public void unload(Player player) {
        this.plugin.getSetListMap().clearSetListMap(player);
    }
}
