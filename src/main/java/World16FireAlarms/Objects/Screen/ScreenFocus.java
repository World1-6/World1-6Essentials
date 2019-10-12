package World16FireAlarms.Objects.Screen;

import World16.Main.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ScreenFocus {

    private Main plugin;

    private Player player;

    private ItemStack[] oldInv;

    public ScreenFocus(Main plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.oldInv = player.getInventory().getContents();
        this.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, true, false));
        this.player.getInventory().clear();
        giveTools();
    }

    public void giveTools() {
        ItemStack DOWN = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta DOWNItemMeta = DOWN.getItemMeta();
        DOWNItemMeta.setDisplayName("DOWN");
        DOWN.setItemMeta(DOWNItemMeta);

        ItemStack SCROLLDOWN = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta SCROLLDOWNItemMeta = SCROLLDOWN.getItemMeta();
        SCROLLDOWNItemMeta.setDisplayName("SCROLL DOWN");
        SCROLLDOWN.setItemMeta(SCROLLDOWNItemMeta);

        ItemStack SCROLLUP = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta SCROLlUPITEMMETA = SCROLLUP.getItemMeta();
        SCROLlUPITEMMETA.setDisplayName("SCROLL UP");
        SCROLLUP.setItemMeta(SCROLlUPITEMMETA);

        ItemStack Exit = new ItemStack(Material.BARRIER);
        ItemMeta ExitItemmeta = Exit.getItemMeta();
        ExitItemmeta.setDisplayName("EXIT");
        Exit.setItemMeta(ExitItemmeta);

        this.player.getInventory().setItem(0, DOWN);
        this.player.getInventory().setItem(2, SCROLLDOWN);
        this.player.getInventory().setItem(3, SCROLLUP);
        this.player.getInventory().setItem(8, Exit);
    }

    public void revert() {
        this.player.getInventory().clear();
        this.player.getInventory().setContents(this.oldInv);

        for (PotionEffect effect : this.player.getActivePotionEffects())
            this.player.removePotionEffect(effect.getType());
    }
}
