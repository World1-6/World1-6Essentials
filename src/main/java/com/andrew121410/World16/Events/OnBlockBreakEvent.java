package com.andrew121410.World16.Events;

import com.andrew121410.World16.Main.Main;
import com.andrew121410.World16FireAlarms.Screen.FireAlarmScreen;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class OnBlockBreakEvent implements Listener {

    private Main plugin;

    private Map<Location, FireAlarmScreen> fireAlarmScreenMap;

    public OnBlockBreakEvent(Main plugin) {
        this.plugin = plugin;

        this.fireAlarmScreenMap = this.plugin.getSetListMap().getFireAlarmScreenMap();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {

        if (fireAlarmScreenMap.get(event.getBlock().getLocation()) != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getFireAlarmManager().deleteFireAlarmSignScreen(event.getBlock().getLocation());
                }
            }.runTaskLater(plugin, 20L);
        }
    }
}
