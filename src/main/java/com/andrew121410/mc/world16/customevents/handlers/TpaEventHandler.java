package com.andrew121410.mc.world16.customevents.handlers;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.customevents.events.TpaCustomEvent;

public class TpaEventHandler {

    public TpaEventHandler(World16Essentials plugin, String p, String target) {
        TpaCustomEvent event = new TpaCustomEvent(plugin, p, target);
        plugin.getServer().getPluginManager().callEvent(event);
    }
}
