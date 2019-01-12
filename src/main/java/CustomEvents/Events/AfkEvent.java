package CustomEvents.Events;

import Main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AfkEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private static Main plugin = Main.getPlugin();

    String p;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    //CODE STARTS HERE

    public AfkEvent(String p) {
        this.p = p;
    }

    public String getPlayerName() {
        return this.p;
    }

    public Player getPlayer() {
        return this.plugin.getServer().getPlayerExact(p);
    }

    public Main getPlugin() {
        return this.plugin;
    }
}
