package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.utils.Utils;
import com.andrew121410.mc.world16utils.utils.ccutils.dependencies.google.common.collect.ArrayListMultimap;
import com.andrew121410.mc.world16utils.utils.ccutils.dependencies.google.common.collect.Multimap;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.ISQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.SQLite;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.EasySQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.MultiTableEasySQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.SQLDataStore;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.sql.SQLException;
import java.util.*;

public class HomeManager {

    private final Map<UUID, Map<String, UnlinkedWorldLocation>> homesMap;

    private final World16Essentials plugin;

    private final ISQL isql;
    private final MultiTableEasySQL multiTableEasySQL;
    private final EasySQL easySQL;

    public HomeManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.homesMap = this.plugin.getMemoryHolder().getHomesMap();

        this.isql = new SQLite(this.plugin.getDataFolder(), "Homes");
        this.multiTableEasySQL = new MultiTableEasySQL(this.isql);
        this.easySQL = new EasySQL("Homes2", this.multiTableEasySQL);

        List<String> columns = new ArrayList<>();
        columns.add("UUID");
        columns.add("HomeName");
        columns.add("Date");
        columns.add("WorldUUID");
        columns.add("X");
        columns.add("Y");
        columns.add("Z");
        columns.add("YAW");
        columns.add("PITCH");
        columns.add("WorldName"); // Not needed but just in case store it.

        easySQL.create(columns, false);

        try {
            convertToNewHomesIfNeeded();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertToNewHomesIfNeeded() throws Exception {
        List<String> tables = this.multiTableEasySQL.getAllTables();

        // Do nothing if the table doesn't exist.
        if (!tables.contains("Homes")) return;

        OldHomeManager oldHomeManager = new OldHomeManager(this.plugin, this.multiTableEasySQL);

        // Load all homes from the old database.
        Map<UUID, Map<String, UnlinkedWorldLocation>> oldHomes = oldHomeManager.loadAllHomesFromDatabase();

        // Save all homes to the new table.
        for (Map.Entry<UUID, Map<String, UnlinkedWorldLocation>> entry : oldHomes.entrySet()) {
            this.saveBulk(entry.getKey(), entry.getValue());
        }

        // Delete the old table.
        this.multiTableEasySQL.deleteTable("Homes");

        this.plugin.getServer().getLogger().info("HomeManager - Converted old home data to the new format successfully.");
    }

    public void load(OfflinePlayer player) {
        this.homesMap.putIfAbsent(player.getUniqueId(), loadHomes(player.getUniqueId()));
    }

    public Map<String, UnlinkedWorldLocation> loadHomes(UUID uuid) {
        Multimap<String, SQLDataStore> multimap = null;

        SQLDataStore toGet = new SQLDataStore();
        toGet.put("UUID", String.valueOf(uuid));
        try {
            multimap = easySQL.get(toGet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (multimap == null) return new HashMap<>();

        Map<String, UnlinkedWorldLocation> homes = new HashMap<>();
        multimap.forEach((key, value) -> {
            String homeName = value.get("HomeName");
            UnlinkedWorldLocation location = getLocation(value);
            homes.put(homeName, location);
        });

        return homes;
    }

    public void add(OfflinePlayer offlinePlayer, String homeName, UnlinkedWorldLocation location) {
        if (offlinePlayer == null) return;

        if (offlinePlayer.isOnline()) {
            this.homesMap.get(offlinePlayer.getUniqueId()).put(homeName, location);
        }

        save(createSQLDataStore(offlinePlayer.getUniqueId(), homeName, location));
    }

    public void add(OfflinePlayer offlinePlayer, Map<String, UnlinkedWorldLocation> map) {
        if (offlinePlayer == null) return;

        if (offlinePlayer.isOnline()) {
            this.homesMap.get(offlinePlayer.getUniqueId()).putAll(map);
        }

        saveBulk(offlinePlayer.getUniqueId(), map);
    }

    public void delete(UUID uuid, String homeName) {
        this.homesMap.get(uuid).remove(homeName.toLowerCase());
        SQLDataStore toDelete = new SQLDataStore();
        toDelete.put("UUID", String.valueOf(uuid));
        toDelete.put("HomeName", homeName.toLowerCase());
        try {
            easySQL.delete(toDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllHomes(UUID uuid) {
        this.homesMap.get(uuid).clear();
        SQLDataStore toDelete = new SQLDataStore();
        toDelete.put("UUID", String.valueOf(uuid));
        try {
            easySQL.delete(toDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(SQLDataStore sqlDataStore) {
        try {
            easySQL.save(sqlDataStore);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveBulk(UUID uuid, Map<String, UnlinkedWorldLocation> map) {
        Multimap<String, SQLDataStore> multimap = ArrayListMultimap.create();

        for (Map.Entry<String, UnlinkedWorldLocation> entry : map.entrySet()) {
            String homeName = entry.getKey();
            UnlinkedWorldLocation location = entry.getValue();
            multimap.put(String.valueOf(uuid), createSQLDataStore(uuid, homeName, location));
        }

        try {
            this.easySQL.save(multimap);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UnlinkedWorldLocation getLocation(SQLDataStore sqlDataStore) {
        String stringWorldUUID = sqlDataStore.get("WorldUUID");
        String stringX = sqlDataStore.get("X");
        String stringY = sqlDataStore.get("Y");
        String stringZ = sqlDataStore.get("Z");
        String stringYaw = sqlDataStore.get("YAW");
        String stringPitch = sqlDataStore.get("PITCH");
        String stringWorldName = sqlDataStore.getOrDefault("WorldName", null);

        UUID worldUUID = null;
        try {
            worldUUID = UUID.fromString(stringWorldUUID);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return new UnlinkedWorldLocation(worldUUID, Double.parseDouble(stringX), Double.parseDouble(stringY), Double.parseDouble(stringZ), Float.parseFloat(stringYaw), Float.parseFloat(stringPitch));
    }

    public SQLDataStore createSQLDataStore(UUID playerUUID, String homeName, UnlinkedWorldLocation location) {
        SQLDataStore sqlDataStore = new SQLDataStore();
        sqlDataStore.put("UUID", String.valueOf(playerUUID));
        sqlDataStore.put("HomeName", homeName.toLowerCase());
        sqlDataStore.put("Date", String.valueOf(System.currentTimeMillis()));
        sqlDataStore.put("WorldUUID", String.valueOf(location.getWorldUUID()));
        sqlDataStore.put("X", String.valueOf(location.getX()));
        sqlDataStore.put("Y", String.valueOf(location.getY()));
        sqlDataStore.put("Z", String.valueOf(location.getZ()));
        sqlDataStore.put("YAW", String.valueOf(location.getYaw()));
        sqlDataStore.put("PITCH", String.valueOf(location.getPitch()));
        // World name is not needed but just in case store it, well if we can.
        World world = location.isWorldLoaded() ? location.getWorld() : null;
        sqlDataStore.put("WorldName", world == null ? "na" : world.getName());

        return sqlDataStore;
    }

    // Used for data translator (EssentialsX, CMI, etc.)
    public Map<UUID, Map<String, UnlinkedWorldLocation>> loadAllHomesFromDatabase() {
        Map<UUID, Map<String, UnlinkedWorldLocation>> bigMap = new HashMap<>();

        try {
            Multimap<String, SQLDataStore> convert = easySQL.getEverything();
            convert.forEach((key, value) -> {
                UUID uuid = UUID.fromString(value.get("UUID"));
                String homeName = value.get("HomeName");
                UnlinkedWorldLocation location = getLocation(value);

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