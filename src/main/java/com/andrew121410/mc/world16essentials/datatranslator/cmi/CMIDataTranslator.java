package com.andrew121410.mc.world16essentials.datatranslator.cmi;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.datatranslator.IDataTranslator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;

public class CMIDataTranslator implements IDataTranslator {

    private final World16Essentials plugin;
    private final CMIReflectionAPI cmiReflectionAPI;

    public CMIDataTranslator(World16Essentials plugin) {
        this.plugin = plugin;
        this.cmiReflectionAPI = new CMIReflectionAPI();
    }

    @Override
    public boolean convertFrom() {
        this.homesFrom();
        this.warpsFrom();
        return true;
    }

    @Override
    public boolean convertTo() {
        this.homesTo();
        this.warpsTo();
        return true;
    }

    private void homesFrom() {
        for (OfflinePlayer offlinePlayer : this.plugin.getServer().getOfflinePlayers()) {
            Map<String, Location> homes = this.cmiReflectionAPI.getHomes(offlinePlayer.getUniqueId());
            if (homes == null) continue;

            for (Map.Entry<String, Location> entry : homes.entrySet()) {
                String homeName = entry.getKey();
                Location location = entry.getValue();

                this.plugin.getHomeManager().add(offlinePlayer, homeName, location);
            }
        }
    }

    private void warpsFrom() {
        Map<String, Location> warps = this.cmiReflectionAPI.getWarps();

        if (warps == null) {
            Bukkit.broadcastMessage("Warps returned null for CMI");
            return;
        }

        warps.forEach((warpName, warpLocation) -> this.plugin.getWarpManager().add(warpName, warpLocation));
    }

    private void homesTo() {
        Map<UUID, Map<String, Location>> allHomes = this.plugin.getHomeManager().loadAllHomesForAllPlayersIncludingOfflinePlayers();

        for (Map.Entry<UUID, Map<String, Location>> uuidMapEntry : allHomes.entrySet()) {
            UUID uuid = uuidMapEntry.getKey();
            Map<String, Location> homes = uuidMapEntry.getValue();

            homes.forEach((homeName, location) -> this.cmiReflectionAPI.addHome(uuid, homeName, location));
        }
    }

    private void warpsTo() {
        this.plugin.getSetListMap().getWarpsMap().forEach(this.cmiReflectionAPI::addWarp);
    }
}
