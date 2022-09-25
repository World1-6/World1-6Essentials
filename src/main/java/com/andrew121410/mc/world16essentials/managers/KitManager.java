package com.andrew121410.mc.world16essentials.managers;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.ISQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.SQLite;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.EasySQL;
import com.andrew121410.mc.world16utils.utils.ccutils.storage.easy.SQLDataStore;
import com.google.common.collect.Multimap;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class KitManager {

    private final Map<String, KitObject> kitsMap;

    private final World16Essentials plugin;

    private final ISQL iSQL;
    private final EasySQL easySQL;

    public KitManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.kitsMap = this.plugin.getSetListMap().getKitsMap();

        this.iSQL = new SQLite(this.plugin.getDataFolder(), "Kits");
        this.easySQL = new EasySQL(this.iSQL, "Kits");

        List<String> columns = new ArrayList<>();

        columns.add("KitName");
        columns.add("WhoCreated");
        columns.add("TimeCreated");
        columns.add("RegularInventory");
        columns.add("ArmorContent");

        easySQL.create(columns, true);
    }

    public void addKit(Player player, String kitName, String[] data) {
        KitObject kitObject = new KitObject(kitName, player.getUniqueId(), System.currentTimeMillis() + "", data);
        this.kitsMap.put(kitName, kitObject);
        saveKit(kitObject);
    }

    public void saveKit(KitObject kitObject) {
        Map<String, String> map = new HashMap<>();

        map.put("KitName", kitObject.getKitName());
        map.put("WhoCreated", kitObject.getWhoCreatedUUID().toString());
        map.put("TimeCreated", kitObject.getTimeCreated());
        map.put("RegularInventory", kitObject.getData()[0]);
        map.put("ArmorContent", kitObject.getData()[1]);

        try {
            this.easySQL.save(map);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteKit(String kitName) {
        // Remove from memory
        this.kitsMap.remove(kitName);

        // Remove from database
        Map<String, String> map = new HashMap<>();
        map.put("KitName", kitName);
        this.easySQL.delete(map);
    }

    public void loadKits() {
        try {
            Multimap<String, SQLDataStore> everything = this.easySQL.getEverything();
            everything.forEach((key, value) -> {
                String kitName = value.get("KitName");
                String whoCreated = value.get("WhoCreated");
                String timeCreated = value.get("TimeCreated");
                String regularInventory = value.get("RegularInventory");
                String armorContent = value.get("ArmorContent");

                KitObject kitObject = new KitObject(kitName, UUID.fromString(whoCreated), timeCreated, new String[]{regularInventory, armorContent});
                this.kitsMap.put(kitName, kitObject);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
