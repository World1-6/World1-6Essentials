package com.andrew121410.mc.world16.managers;

import com.andrew121410.CCUtils.storage.ISQL;
import com.andrew121410.CCUtils.storage.SQLite;
import com.andrew121410.CCUtils.storage.easy.EasySQL;
import com.andrew121410.CCUtils.storage.easy.SQLDataStore;
import com.andrew121410.mc.world16.Main;
import com.google.common.collect.Multimap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class HomeManager {

    private Map<UUID, Map<String, Location>> homesMap;

    private Main plugin;

    private EasySQL easySQL;
    private ISQL isql;

    public HomeManager(Main plugin) {
        this.plugin = plugin;
        this.homesMap = this.plugin.getSetListMap().getHomesMap();

        this.isql = new SQLite(this.plugin.getDataFolder(), "Homes");
        this.easySQL = new EasySQL(isql, "Homes");

        List<String> columns = new ArrayList<>();
        columns.add("UUID");
        columns.add("Date");
        columns.add("PlayerName");
        columns.add("HomeName");
        columns.add("X");
        columns.add("Y");
        columns.add("Z");
        columns.add("YAW");
        columns.add("PITCH");
        columns.add("World");
        easySQL.create(columns, false);
    }

    public void load(Player player) {
        Map<String, String> toGet = new HashMap<>();
        toGet.put("UUID", String.valueOf(player.getUniqueId()));
        Multimap<String, SQLDataStore> convert = easySQL.get(toGet);
        this.homesMap.putIfAbsent(player.getUniqueId(), new HashMap<>());
        convert.forEach((k, v) -> load(player, v));
    }

    public void save(UUID uuid, String playerName, String homeName, Location location) {
        SQLDataStore sqlDataStore = new SQLDataStore();
        sqlDataStore.getMap().put("UUID", String.valueOf(uuid));
        sqlDataStore.getMap().put("Date", "0");
        sqlDataStore.getMap().put("PlayerName", playerName);
        sqlDataStore.getMap().put("HomeName", homeName.toLowerCase());
        sqlDataStore.getMap().put("X", String.valueOf(location.getX()));
        sqlDataStore.getMap().put("Y", String.valueOf(location.getY()));
        sqlDataStore.getMap().put("Z", String.valueOf(location.getZ()));
        sqlDataStore.getMap().put("YAW", String.valueOf(location.getYaw()));
        sqlDataStore.getMap().put("PITCH", String.valueOf(location.getPitch()));
        sqlDataStore.getMap().put("World", location.getWorld().getName());
        try {
            easySQL.save(sqlDataStore);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID uuid, String homeName) {
        this.homesMap.get(uuid).remove(homeName.toLowerCase());
        Map<String, String> toDelete = new HashMap<>();
        toDelete.put("UUID", String.valueOf(uuid));
        toDelete.put("HomeName", homeName.toLowerCase());
        easySQL.delete(toDelete);
    }

    public void deleteALL(UUID uuid) {
        this.homesMap.get(uuid).clear();
        Map<String, String> toDelete = new HashMap<>();
        toDelete.put("UUID", String.valueOf(uuid));
        easySQL.delete(toDelete);
    }

    private void load(Player player, SQLDataStore sqlDataStore) {
        String UUID = sqlDataStore.getMap().get("UUID");
        String Date = sqlDataStore.getMap().get("Date");
        String PlayerName = sqlDataStore.getMap().get("PlayerName");
        String HomeName = sqlDataStore.getMap().get("HomeName");
        String X = sqlDataStore.getMap().get("X");
        String Y = sqlDataStore.getMap().get("Y");
        String Z = sqlDataStore.getMap().get("Z");
        String YAW = sqlDataStore.getMap().get("YAW");
        String PITCH = sqlDataStore.getMap().get("PITCH");
        String World = sqlDataStore.getMap().get("World");

        Location location = new Location(this.plugin.getServer().getWorld(World), Double.parseDouble(X), Double.parseDouble(Y), Double.parseDouble(Z), Float.parseFloat(YAW), Float.parseFloat(PITCH));
        this.homesMap.get(player.getUniqueId()).put(HomeName, location);
    }
}