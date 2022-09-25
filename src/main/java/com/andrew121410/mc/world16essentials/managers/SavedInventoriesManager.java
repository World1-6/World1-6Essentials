package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.SavedInventoryObject;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.ISQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.SQLite;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.EasySQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.SQLDataStore;
import com.google.common.collect.Multimap;

import java.sql.SQLException;
import java.util.*;

public class SavedInventoriesManager {

    private final Map<UUID, Set<String>> savedInventoryMap;

    private final World16Essentials plugin;

    private final ISQL iSQL;
    private final EasySQL easySQL;

    public SavedInventoriesManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.savedInventoryMap = this.plugin.getSetListMap().getSavedInventoryMap();

        this.iSQL = new SQLite(this.plugin.getDataFolder(), "SavedInventories");
        this.easySQL = new EasySQL(this.iSQL, "SavedInventories");

        List<String> columns = new ArrayList<>();

        columns.add("UUID");
        columns.add("InventoryName");
        columns.add("RegularInventory");
        columns.add("ArmorContent");

        easySQL.create(columns, false);
    }

    public void save(UUID uuid, String name, String[] data) {
        this.savedInventoryMap.get(uuid).add(name);

        Map<String, String> map = new HashMap<>();

        map.put("UUID", uuid.toString());
        map.put("InventoryName", name);
        map.put("RegularInventory", data[0]);
        map.put("ArmorContent", data[1]);

        try {
            this.easySQL.save(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SavedInventoryObject load(UUID uuid, String inventoryName) {
        Map<String, String> map = new HashMap<>();

        map.put("UUID", uuid.toString());
        map.put("InventoryName", inventoryName);

        SQLDataStore sqlDataStore = this.easySQL.get(map).values().stream().findFirst().get();

        String regularInventory = sqlDataStore.get("RegularInventory");
        String armorContent = sqlDataStore.get("ArmorContent");
        String[] data = new String[]{regularInventory, armorContent};

        return new SavedInventoryObject(inventoryName, data);
    }

    public void loadAllSavedInventoriesNames(UUID uuid) {
        this.savedInventoryMap.put(uuid, new HashSet<>());

        Map<String, String> map = new HashMap<>();
        map.put("UUID", uuid.toString());

        Multimap<String, SQLDataStore> multimap = this.easySQL.get(map);
        multimap.forEach((s, sqlDataStore) -> {
            String inventoryName = sqlDataStore.get("InventoryName");
            this.savedInventoryMap.get(uuid).add(inventoryName);
        });
    }

    public void delete(UUID uuid, String inventoryName) {
        this.savedInventoryMap.get(uuid).remove(inventoryName);

        Map<String, String> map = new HashMap<>();
        map.put("UUID", uuid.toString());
        map.put("InventoryName", inventoryName);

        this.easySQL.delete(map);
    }
}
