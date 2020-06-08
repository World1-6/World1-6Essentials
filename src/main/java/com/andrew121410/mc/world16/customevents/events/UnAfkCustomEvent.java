package com.andrew121410.mc.world16.customevents.events;

import com.andrew121410.mc.world16.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This runs when the player gets back from afk
 *
 * @author Andrew121410
 */
public class UnAfkCustomEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Main plugin;

    String p;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

//CODE STARTS HERE

    public UnAfkCustomEvent(Main plugin, String p) {
        this.plugin = plugin;
        this.p = p;
    }

    /**
     * Get's the player name
     *
     * @return Player name
     */
    public String getPlayerName() {
        return this.p;
    }

    /**
     * Get's the Player Object
     *
     * @return Player Object
     */
    public Player getPlayer() {
        return plugin.getServer().getPlayerExact(p);
    }

    /**
     * Get's the plugin
     *
     * @return Plugin
     */
    public Main getPlugin() {
        return plugin;
    }
}