package com.andrew121410.mc.world16essentials.objects;

import com.andrew121410.mc.world16utils.utils.BukkitSerialization;
import org.bukkit.entity.Player;

public class SavedInventoryObject {

    private final String key;
    private final String[] data;

    public SavedInventoryObject(String key, String[] data) {
        this.key = key;
        this.data = data;
    }

    public void give(Player player) {
        BukkitSerialization.giveFromBase64s(player, this.data);
    }

    public String getKey() {
        return key;
    }

    public String[] getData() {
        return data;
    }
}
