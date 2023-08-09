package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;

public class World16Utils {

    public void onEnable(World16Essentials plugin) {

        // Register listeners
        new OnInventoryClickEvent(plugin);
        new OnInventoryCloseEvent(plugin);
    }
}
