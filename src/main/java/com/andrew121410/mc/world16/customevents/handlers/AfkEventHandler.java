package com.andrew121410.mc.world16.customevents.handlers;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.customevents.events.AfkCustomEvent;

public class AfkEventHandler {

    public AfkEventHandler(World16Essentials plugin, String p) {
        AfkCustomEvent event = new AfkCustomEvent(plugin, p);
        plugin.getServer().getPluginManager().callEvent(event);
    }
}
