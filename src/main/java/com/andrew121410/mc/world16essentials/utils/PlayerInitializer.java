package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.commands.back.BackEnum;
import com.andrew121410.mc.world16essentials.objects.AfkObject;
import com.andrew121410.mc.world16essentials.objects.PowerToolObject;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerInitializer {

    private final Map<UUID, Map<BackEnum, Location>> backMap;
    private final Map<UUID, PowerToolObject> powerToolMap;
    private final Map<UUID, AfkObject> afkObjectMap;
    private final Map<UUID, Long> timeOfLoginMap;

    private final List<UUID> hiddenPlayersList;

    private final World16Essentials plugin;
    private final API api;

    public PlayerInitializer(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.backMap = this.plugin.getSetListMap().getBackMap();
        this.hiddenPlayersList = this.plugin.getSetListMap().getHiddenPlayers();
        this.powerToolMap = this.plugin.getSetListMap().getPowerToolMap();
        this.afkObjectMap = this.plugin.getSetListMap().getAfkMap();
        this.timeOfLoginMap = this.plugin.getSetListMap().getTimeOfLoginMap();
    }

    public void load(Player player) {
        backMap.put(player.getUniqueId(), new HashMap<>());
        powerToolMap.put(player.getUniqueId(), new PowerToolObject());
        timeOfLoginMap.put(player.getUniqueId(), System.currentTimeMillis());

        this.plugin.getHomeManager().load(player);
        this.afkObjectMap.put(player.getUniqueId(), new AfkObject(player));
        this.plugin.getSavedInventoriesManager().loadAllSavedInventoriesNames(player.getUniqueId());

        String color = player.isOp() ? "&4" : "&7";
        for (UUID uuid : hiddenPlayersList) {
            Player target = this.plugin.getServer().getPlayer(uuid);
            if (target == null) continue;
            player.hidePlayer(this.plugin, target);
            target.sendMessage(Translate.chat(api.getMessagesUtils().getPrefix() + " " + color + player.getDisplayName() + " &cnow cannot see you,"));
        }
    }

    public void unload(Player player) {
        this.plugin.getSetListMap().clearSetListMap(player);
    }
}
