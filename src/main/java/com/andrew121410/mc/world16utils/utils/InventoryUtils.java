package com.andrew121410.mc.world16utils.utils;

import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

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

    public static ItemStack createItem(Material material, int amount, String displayName) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Translate.color(displayName));
        item.setItemMeta(itemMeta);
        return item;
    }
}
