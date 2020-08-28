package com.andrew121410.mc.world16.utils;

import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

    public InventoryUtils() {
    }

    public static ItemStack createItem(Material material, int amount, String displayName, String... loreString) {
        List<String> lore = new ArrayList<>();
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Translate.color(displayName));
        for (String s : loreString) lore.add(Translate.chat(s));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static int getNextInvFreeSpot(Inventory inventory) {
        //THIS IS THE OLD WAY THE NEW WAY IS https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/Inventory.html#firstEmpty--
        int i = 1;
        for (ItemStack is : inventory.getContents()) {
            if (is == null)
                continue;
            i++;
        }
        return i;
    }
}
