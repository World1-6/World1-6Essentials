package com.andrew121410.mc.world16.events;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.objects.LocationObject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Map;
import java.util.UUID;

public class OnPlayerDeathEvent implements Listener {

    private Main plugin;

    private Map<UUID, LocationObject> backMap;

    public OnPlayerDeathEvent(Main plugin) {
        this.plugin = plugin;
        this.backMap = this.plugin.getSetListMap().getBackM();
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void OnDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();

        LocationObject back = this.backMap.get(p.getUniqueId());
        if (back != null) {
            back.setLocation("death", 1, p.getLocation());
        } else {
            LocationObject locationObject = new LocationObject();
            locationObject.setLocation("death", 1, p.getLocation());
            backMap.put(p.getUniqueId(), locationObject);
        }
    }
}