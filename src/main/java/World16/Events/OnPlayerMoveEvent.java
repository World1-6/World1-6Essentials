package World16.Events;

import World16.Main.Main;
import World16.Utils.API;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEvent implements Listener {

    private Main plugin;
    private API api;

    public OnPlayerMoveEvent(Main plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onMoveM(PlayerMoveEvent event) {
        Player p = event.getPlayer();
    }
}
