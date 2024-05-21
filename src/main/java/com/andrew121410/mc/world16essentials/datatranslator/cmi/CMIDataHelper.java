package com.andrew121410.mc.world16essentials.datatranslator.cmi;

import com.Zrips.CMI.Containers.CMIPlayerInventory;
import com.Zrips.CMI.Modules.Kits.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CMIDataHelper {

    public static List<ItemStack> getExtraItems(Kit kit) {
        List<ItemStack> itemStacks = new ArrayList<>();

        ItemStack boots = kit.getExtraItem(CMIPlayerInventory.CMIInventorySlot.Boots);
        ItemStack leggings = kit.getExtraItem(CMIPlayerInventory.CMIInventorySlot.Pants);
        ItemStack chestplate = kit.getExtraItem(CMIPlayerInventory.CMIInventorySlot.ChestPlate);
        ItemStack helmet = kit.getExtraItem(CMIPlayerInventory.CMIInventorySlot.Helmet);

        ItemStack offHand = kit.getExtraItem(CMIPlayerInventory.CMIInventorySlot.OffHand);

        itemStacks.add(Objects.requireNonNullElseGet(boots, () -> new ItemStack(Material.AIR)));
        itemStacks.add(Objects.requireNonNullElseGet(leggings, () -> new ItemStack(Material.AIR)));
        itemStacks.add(Objects.requireNonNullElseGet(chestplate, () -> new ItemStack(Material.AIR)));
        itemStacks.add(Objects.requireNonNullElseGet(helmet, () -> new ItemStack(Material.AIR)));

        itemStacks.add(Objects.requireNonNullElseGet(offHand, () -> new ItemStack(Material.AIR)));

        return itemStacks;
    }

    public static void setExtraItems(Kit kit, List<ItemStack> itemStacks) {
        kit.setExtraItem(CMIPlayerInventory.CMIInventorySlot.Boots, itemStacks.get(0));
        kit.setExtraItem(CMIPlayerInventory.CMIInventorySlot.Pants, itemStacks.get(1));
        kit.setExtraItem(CMIPlayerInventory.CMIInventorySlot.ChestPlate, itemStacks.get(2));
        kit.setExtraItem(CMIPlayerInventory.CMIInventorySlot.Helmet, itemStacks.get(3));

        kit.setExtraItem(CMIPlayerInventory.CMIInventorySlot.OffHand, itemStacks.get(4));
    }

    public static List<ItemStack> adjustItemsForCMIKits(List<ItemStack> items) {
        List<ItemStack> adjustedItems = new ArrayList<>(Collections.nCopies(36, null));

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            if (item == null) continue;
            int cmiSlot = getCMISlot(i);
            adjustedItems.set(cmiSlot, item);
        }

        return adjustedItems;
    }

    private static int getCMISlot(int minecraftSlot) {
        if (minecraftSlot >= 0 && minecraftSlot <= 8) {
            // Hotbar: Minecraft 0-8 -> CMI 27-35
            return minecraftSlot + 27;
        } else if (minecraftSlot >= 9 && minecraftSlot <= 35) {
            // Main Inventory: Minecraft 9-35 -> CMI 0-26
            return minecraftSlot - 9;
        }
        throw new IllegalArgumentException("Invalid Minecraft slot: " + minecraftSlot);
    }
}
