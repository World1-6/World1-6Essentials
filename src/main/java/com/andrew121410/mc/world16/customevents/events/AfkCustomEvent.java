package com.andrew121410.mc.world16.customevents.events;

import com.andrew121410.mc.world16.World16Essentials;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Everytime someone goes AFK this event will be run]
 *
 * @author Andrew121410
 */
public class AfkCustomEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private World16Essentials plugin;

    String p;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    //CODE STARTS HERE

    public AfkCustomEvent(World16Essentials plugin, String p) {
        this.plugin = plugin;
        this.p = p;
    }

    /**
     * Get's the player name that did /afk
     *
     * @return The name of player
     */
    public String getPlayerName() {
        return this.p;
    }

    /**
     * Get's the Player Object
     *
     * @return The Player Object
     */
    public Player getPlayer() {
        return plugin.getServer().getPlayerExact(p);
    }

    /**
     * Get's the plugin
     *
     * @return Returns the plugin
     */
    public World16Essentials getPlugin() {
        return plugin;
    }
}
