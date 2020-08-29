package com.andrew121410.mc.world16.customevents.handlers;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.customevents.events.UnAfkCustomEvent;

public class UnAfkEventHandler {

    public UnAfkEventHandler(Main plugin, String p) {
        UnAfkCustomEvent event = new UnAfkCustomEvent(plugin, p);
        plugin.getServer().getPluginManager().callEvent(event);
    }
}
