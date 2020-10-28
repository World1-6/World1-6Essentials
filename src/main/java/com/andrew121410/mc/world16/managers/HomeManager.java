package com.andrew121410.mc.world16.managers;

import com.andrew121410.ccutils.storage.ISQL;
import com.andrew121410.ccutils.storage.SQLite;
import com.andrew121410.ccutils.storage.easy.EasySQL;
import com.andrew121410.ccutils.storage.easy.SQLDataStore;
import com.andrew121410.mc.world16.World16Essentials;
import com.google.common.collect.Multimap;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class HomeManager {

    private Map<UUID, Map<String, Location>> homesMap;

    private World16Essentials plugin;

    private EasySQL easySQL;
    private ISQL isql;

    public HomeManager(World16Essentials plugin) {
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
        sqlDataStore.put("UUID", String.valueOf(uuid));
        sqlDataStore.put("Date", "0");
        sqlDataStore.put("PlayerName", playerName);
        sqlDataStore.put("HomeName", homeName.toLowerCase());
        sqlDataStore.put("X", String.valueOf(location.getX()));
        sqlDataStore.put("Y", String.valueOf(location.getY()));
        sqlDataStore.put("Z", String.valueOf(location.getZ()));
        sqlDataStore.put("YAW", String.valueOf(location.getYaw()));
        sqlDataStore.put("PITCH", String.valueOf(location.getPitch()));
        sqlDataStore.put("World", location.getWorld().getName());
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
        String UUID = sqlDataStore.get("UUID");
        String Date = sqlDataStore.get("Date");
        String PlayerName = sqlDataStore.get("PlayerName");
        String HomeName = sqlDataStore.get("HomeName");
        String X = sqlDataStore.get("X");
        String Y = sqlDataStore.get("Y");
        String Z = sqlDataStore.get("Z");
        String YAW = sqlDataStore.get("YAW");
        String PITCH = sqlDataStore.get("PITCH");
        String World = sqlDataStore.get("World");

        Location location = new Location(this.plugin.getServer().getWorld(World), Double.parseDouble(X), Double.parseDouble(Y), Double.parseDouble(Z), Float.parseFloat(YAW), Float.parseFloat(PITCH));
        this.homesMap.get(player.getUniqueId()).put(HomeName, location);
    }
}