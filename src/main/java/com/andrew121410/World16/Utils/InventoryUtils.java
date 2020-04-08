package com.andrew121410.World16.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

    public InventoryUtils() {

    }

    public static ItemStack createItem(Inventory inv, Material material, int ammount, int invSlot, String displayName, String... loreString) {
        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(material, ammount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Translate.chat(displayName));
        for (String s : loreString) {
            lore.add(Translate.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static ItemStack createItem(Material material, int ammount, int invSlot, String displayName, String... loreString) {
        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(material, ammount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Translate.chat(displayName));
        for (String s : loreString) {
            lore.add(Translate.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItem(Inventory inv, Material material, int ammount, String displayName, String... loreString) {
        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(material, ammount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Translate.chat(displayName));
        for (String s : loreString) {
            lore.add(Translate.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItem(Material material, int ammount, String displayName, String... loreString) {
        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(material, ammount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Translate.chat(displayName));
        for (String s : loreString) {
            lore.add(Translate.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createItemWithByte(Inventory inv, Material material, int byteID, int ammount, int invSlot, String displayName, String... loreString) {
        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(material, ammount, (short) byteID);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Translate.chat(displayName));
        for (String s : loreString) {
            lore.add(Translate.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        inv.setItem(invSlot - 1, item);
        return item;
    }

    public static ItemStack createItemWithByte(Material material, int byteID, int ammount, int invSlot, String displayName, String... loreString) {
        ItemStack item;
        List<String> lore = new ArrayList();

        item = new ItemStack(material, ammount, (short) byteID);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Translate.chat(displayName));
        for (String s : loreString) {
            lore.add(Translate.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

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
