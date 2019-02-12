package World16.Events;

import World16.CustomInventorys.CustomInventoryManager;
import World16.Main.Main;
import World16.Utils.ICustomInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClickEvent implements Listener {

    private Main plugin;

    public InventoryClickEvent(Main plugin) {
        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        String inv_name = event.getInventory().getTitle();
        final InventoryHolder holder = event.getInventory().getHolder();

        if (inv_name.equals(CustomInventoryManager.getTestMenu().getInventoryName()) && (event.getCurrentItem() != null) && (!event.getCurrentItem().getType().equals(Material.AIR)) && (event.getClickedInventory() != null) && (holder instanceof ICustomInventory)) {
            event.setCancelled(true);
            CustomInventoryManager.getTestMenu().clicked((Player) event.getWhoClicked(), event.getSlot(), event.getCurrentItem(), event.getInventory());
        } else {
            return;
        }
    }
}
