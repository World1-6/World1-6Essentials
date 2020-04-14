package com.andrew121410.mc.world16.customevents.handlers;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.customevents.Events.AfkCustomEvent;

public class AfkEventHandler {

    public AfkEventHandler(Main plugin, String p) {
        AfkCustomEvent event = new AfkCustomEvent(plugin, p);

        plugin.getServer().getPluginManager().callEvent(event);
    }
}
