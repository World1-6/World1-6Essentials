package com.andrew121410.mc.world16.customevents.handlers;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16.customevents.events.TpaCustomEvent;

public class TpaEventHandler {

    public TpaEventHandler(Main plugin, String p, String target) {
        TpaCustomEvent event = new TpaCustomEvent(plugin, p, target);

        plugin.getServer().getPluginManager().callEvent(event);
    }
}
