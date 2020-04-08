package com.andrew121410.World16.CustomEvents.Events;

import com.andrew121410.World16.Main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * When someone makes a tpa request this event runs
 *
 * @author Andrew121410
 */
public class TpaCustomEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Main plugin;

    String p;
    String target;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    //CODE STARTS HERE

    public TpaCustomEvent(Main plugin, String p, String target) {
        this.plugin = plugin;
        this.p = p;
        this.target = target;
    }

    /**
     * Get's the player name that did the tpa request
     *
     * @return Returns the name of the player
     */
    public String getPlayerName() {
        return this.p;
    }

    /**
     * Get's the Player object from the player
     *
     * @return Returns the Player Object
     */
    public Player getPlayer() {
        return plugin.getServer().getPlayerExact(p);
    }

    /**
     * Get's the plugin
     *
     * @return the plugin
     */
    public Main getPlugin() {
        return plugin;
    }

    /**
     * Get's the target name that the player sent the request too
     *
     * @return the Target name
     */
    public String getTargetName() {
        return this.target;
    }

    /**
     * Get's the Target Player Object that the player sent the request too
     *
     * @return Player Object
     */
    public Player getTargetPlayer() {
        if (target != null) {
            return plugin.getServer().getPlayerExact(target);
        }
        return null;
    }
}
