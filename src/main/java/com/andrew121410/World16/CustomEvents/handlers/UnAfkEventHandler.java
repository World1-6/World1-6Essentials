package com.andrew121410.World16.CustomEvents.handlers;

import com.andrew121410.World16.CustomEvents.Events.UnAfkCustomEvent;
import com.andrew121410.World16.Main.Main;

public class UnAfkEventHandler {

    public UnAfkEventHandler(Main plugin, String p) {
        UnAfkCustomEvent event = new UnAfkCustomEvent(plugin, p);

        plugin.getServer().getPluginManager().callEvent(event);
    }
}
