package com.andrew121410.mc.world16.customevents.handlers;

import com.andrew121410.mc.world16.World16Essentials;
import com.andrew121410.mc.world16.customevents.events.UnAfkCustomEvent;

public class UnAfkEventHandler {

    public UnAfkEventHandler(World16Essentials plugin, String p) {
        UnAfkCustomEvent event = new UnAfkCustomEvent(plugin, p);
        plugin.getServer().getPluginManager().callEvent(event);
    }
}
