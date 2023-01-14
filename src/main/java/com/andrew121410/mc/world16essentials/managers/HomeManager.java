package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.utils.Utils;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.ISQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.SQLite;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.EasySQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.MultiTableEasySQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.SQLDataStore;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.sql.SQLException;
import java.util.*;

public class HomeManager {

    private final Map<UUID, Map<String, Location>> homesMap;

    private final World16Essentials plugin;

    private final EasySQL easySQL;
    private final ISQL isql;

    public HomeManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.homesMap = this.plugin.getSetListMap().getHomesMap();

        this.isql = new SQLite(this.plugin.getDataFolder(), "Homes");
        this.easySQL = new EasySQL("Homes", new MultiTableEasySQL(this.isql));

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

    public void load(OfflinePlayer player) {
        this.homesMap.putIfAbsent(player.getUniqueId(), loadHomes(player.getUniqueId()));
    }

    public Map<String, Location> loadHomes(UUID uuid) {
        Multimap<String, SQLDataStore> multimap = null;

        SQLDataStore toGet = new SQLDataStore();
        toGet.put("UUID", String.valueOf(uuid));
        try {
            multimap = easySQL.get(toGet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (multimap == null) return new HashMap<>();

        Map<String, Location> homes = new HashMap<>();
        multimap.forEach((key, value) -> {
            String homeName = value.get("HomeName");
            Location location = getLocation(value);
            homes.put(homeName, location);
        });

        return homes;
    }

    public void add(OfflinePlayer offlinePlayer, String homeName, Location location) {
        if (offlinePlayer == null) return;

        if (offlinePlayer.isOnline()) {
            this.homesMap.get(offlinePlayer.getUniqueId()).put(homeName, location);
        }

        save(make(offlinePlayer.getUniqueId(), offlinePlayer.getName(), homeName, location));
    }

    public void add(OfflinePlayer offlinePlayer, Map<String, Location> map) {
        if (offlinePlayer == null) return;

        if (offlinePlayer.isOnline()) {
            this.homesMap.get(offlinePlayer.getUniqueId()).putAll(map);
        }

        saveBulk(offlinePlayer.getUniqueId(), map);
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

    public Location getLocation(SQLDataStore sqlDataStore) {
        String X = sqlDataStore.get("X");
        String Y = sqlDataStore.get("Y");
        String Z = sqlDataStore.get("Z");
        String YAW = sqlDataStore.get("YAW");
        String PITCH = sqlDataStore.get("PITCH");
        String World = sqlDataStore.get("World");

        return new Location(this.plugin.getServer().getWorld(World), Double.parseDouble(X), Double.parseDouble(Y), Double.parseDouble(Z), Float.parseFloat(YAW), Float.parseFloat(PITCH));
    }

    public SQLDataStore make(UUID uuid, String playerName, String homeName, Location location) {
        SQLDataStore sqlDataStore = new SQLDataStore();
        sqlDataStore.put("UUID", String.valueOf(uuid));
        sqlDataStore.put("Date", String.valueOf(System.currentTimeMillis()));
        sqlDataStore.put("PlayerName", playerName);
        sqlDataStore.put("HomeName", homeName.toLowerCase());
        sqlDataStore.put("X", String.valueOf(location.getX()));
        sqlDataStore.put("Y", String.valueOf(location.getY()));
        sqlDataStore.put("Z", String.valueOf(location.getZ()));
        sqlDataStore.put("YAW", String.valueOf(location.getYaw()));
        sqlDataStore.put("PITCH", String.valueOf(location.getPitch()));
        sqlDataStore.put("World", location.getWorld().getName());
        return sqlDataStore;
    }

    public void saveBulk(UUID uuid, Map<String, Location> map) {
        Multimap<String, SQLDataStore> multimap = ArrayListMultimap.create();

        for (Map.Entry<String, Location> entry : map.entrySet()) {
            String homeName = entry.getKey();
            Location location = entry.getValue();
            multimap.put(String.valueOf(uuid), make(uuid, "na", homeName, location));
        }

        try {
            this.easySQL.save(multimap);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(SQLDataStore sqlDataStore) {
        try {
            easySQL.save(sqlDataStore);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Map<String, Location>> loadAllHomesFromDatabase() {
        Map<UUID, Map<String, Location>> bigMap = new HashMap<>();

        try {
            Multimap<String, SQLDataStore> convert = easySQL.getEverything();
            convert.forEach((key, value) -> {
                UUID uuid = UUID.fromString(value.get("UUID"));
                String homeName = value.get("HomeName");
                Location location = getLocation(value);

                if (!bigMap.containsKey(uuid)) bigMap.put(uuid, new HashMap<>());

                bigMap.get(uuid).put(homeName, location);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bigMap;
    }

    public int getMaximumHomeCount(Player player) {
        //@TODO should we have some sort of cache for this?

        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            String permission = permissionAttachmentInfo.getPermission();
            if (permission.startsWith("world16.home.") || permission.startsWith("world16.homes.")) {
                return Utils.asIntegerOrElse(permission.substring(permission.lastIndexOf(".") + 1), -1);
            }
        }
        return -1;
    }
}