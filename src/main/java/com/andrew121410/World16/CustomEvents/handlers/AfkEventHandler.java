package com.andrew121410.World16.CustomEvents.handlers;

import com.andrew121410.World16.CustomEvents.Events.AfkCustomEvent;
import com.andrew121410.World16.Main.Main;

public class AfkEventHandler {

    public AfkEventHandler(Main plugin, String p) {
        AfkCustomEvent event = new AfkCustomEvent(plugin, p);

        plugin.getServer().getPluginManager().callEvent(event);
    }
}
