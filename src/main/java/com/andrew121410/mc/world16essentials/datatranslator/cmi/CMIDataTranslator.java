package com.andrew121410.mc.world16essentials.datatranslator.cmi;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.Modules.Homes.CmiHome;
import com.Zrips.CMI.Modules.Warps.CmiWarp;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.datatranslator.IDataTranslator;
import net.Zrips.CMILib.Container.CMILocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class CMIDataTranslator implements IDataTranslator {

    private final World16Essentials plugin;
    private final CMI cmi;

    public CMIDataTranslator(World16Essentials plugin) {
        this.plugin = plugin;
        this.cmi = (CMI) Bukkit.getPluginManager().getPlugin("CMI");
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
            CMIUser cmiUser = this.cmi.getPlayerManager().getUser(offlinePlayer.getUniqueId());

            LinkedHashMap<String, CmiHome> homes = cmiUser.getHomes();
            if (homes == null) continue;

            for (Map.Entry<String, CmiHome> entry : homes.entrySet()) {
                String homeName = entry.getKey();
                CmiHome cmiHome = entry.getValue();
                Location location = cmiHome.getLoc();

                this.plugin.getHomeManager().add(offlinePlayer, homeName, location);
            }
        }
    }

    private void warpsFrom() {
        this.cmi.getWarpManager().getWarps().forEach((warpName, warp) -> {
            Location location = warp.getLoc();
            this.plugin.getWarpManager().add(warpName, location);
        });
    }

    private void homesTo() {
        Map<UUID, Map<String, Location>> allHomes = this.plugin.getHomeManager().loadAllHomesForAllPlayersIncludingOfflinePlayers();

        for (Map.Entry<UUID, Map<String, Location>> uuidMapEntry : allHomes.entrySet()) {
            UUID uuid = uuidMapEntry.getKey();
            Map<String, Location> homes = uuidMapEntry.getValue();

            CMIUser cmiUser = this.cmi.getPlayerManager().getUser(uuid);

            homes.forEach((homeName, location) -> {
                cmiUser.addHome(new CmiHome(homeName, new CMILocation(location)), true);
            });
        }
    }

    private void warpsTo() {
        this.plugin.getSetListMap().getWarpsMap().forEach((warpName, location) -> {
            this.cmi.getWarpManager().addWarp(new CmiWarp(warpName, new CMILocation(location)));
        });
    }
}
