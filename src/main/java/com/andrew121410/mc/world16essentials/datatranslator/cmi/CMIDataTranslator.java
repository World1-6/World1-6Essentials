package com.andrew121410.mc.world16essentials.datatranslator.cmi;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.Modules.Homes.CmiHome;
import com.Zrips.CMI.Modules.Kits.Kit;
import com.Zrips.CMI.Modules.SavedInv.CMIInventory;
import com.Zrips.CMI.Modules.SavedInv.SavedInventories;
import com.Zrips.CMI.Modules.Warps.CmiWarp;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.datatranslator.IDataTranslator;
import com.andrew121410.mc.world16essentials.objects.KitObject;
import com.andrew121410.mc.world16essentials.objects.SavedInventoryObject;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.utils.BukkitSerialization;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import net.Zrips.CMILib.Container.CMILocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CMIDataTranslator implements IDataTranslator {

    private final World16Essentials plugin;
    private final CMI cmi;

    public CMIDataTranslator(World16Essentials plugin, CMI cmi) {
        this.plugin = plugin;
        this.cmi = cmi;
    }

    @Override
    public boolean convertFrom(Player player) {
        this.homesFrom();
        this.warpsFrom();
        this.kitsFrom();
        this.savedInventoriesFrom();
        return true;
    }

    @Override
    public boolean convertTo(Player player) {
        this.homesTo();
        this.warpsTo();
        this.kitsTo();
        this.savedInventoriesTo();
        return true;
    }

    private void homesFrom() {
        for (OfflinePlayer offlinePlayer : this.plugin.getServer().getOfflinePlayers()) {
            CMIUser cmiUser = this.cmi.getPlayerManager().getUser(offlinePlayer);

            LinkedHashMap<String, CmiHome> homes = cmiUser.getHomes();
            if (homes == null) continue;

            for (Map.Entry<String, CmiHome> entry : homes.entrySet()) {
                String homeName = entry.getKey();
                CmiHome cmiHome = entry.getValue();
                Location location = cmiHome.getLoc();

                this.plugin.getHomeManager().add(offlinePlayer, homeName, new UnlinkedWorldLocation(location));
            }
        }
    }

    private void warpsFrom() {
        this.cmi.getWarpManager().getWarps().forEach((warpName, warp) -> {
            CMILocation cmiLocation = warp.getLoc();
            this.plugin.getWarpManager().add(warpName, new Location(cmiLocation.getWorld(), cmiLocation.getX(), cmiLocation.getY(), cmiLocation.getZ(), cmiLocation.getYaw(), cmiLocation.getPitch()));
        });
    }

    private void kitsFrom() {
        this.cmi.getKitsManager().getKitMap().forEach((kitName, kit) -> {
            List<ItemStack> originalKitItems = kit.getItems();

            // Copy the items to a new list (need to clone the items)
            List<ItemStack> kitItems = new ArrayList<>();
            for (ItemStack originalKitItem : originalKitItems) {
                if (originalKitItem == null || originalKitItem.getType() == Material.AIR) {
                    kitItems.add(null);
                    continue;
                }
                kitItems.add(originalKitItem.clone());
            }

            // Reverse the order of the items
            Collections.reverse(kitItems);

            // Then split the items into the hot bar and the rest of the inventory
            List<ItemStack> hotBar = new ArrayList<>();
            List<ItemStack> inventory = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                hotBar.add(kitItems.get(i));
            }
            for (int i = 9; i < kitItems.size(); i++) {
                inventory.add(kitItems.get(i));
            }

            // Reverse the order of the hot bar items
            Collections.reverse(hotBar);

            // Reverse the order of the inventory items
            Collections.reverse(inventory);

            // Clear the kit items list
            kitItems.clear();

            // Add the hot bar items and the inventory items to the kit items list
            kitItems.addAll(hotBar);
            kitItems.addAll(inventory);

            List<ItemStack> originalKitExtraItems = CMIDataHelper.getExtraItems(kit);

            // Copy the items to a new list (need to clone the items)
            List<ItemStack> kitExtraItems = new ArrayList<>();
            for (ItemStack originalKitExtraItem : originalKitExtraItems) {
                if (originalKitExtraItem == null || originalKitExtraItem.getType() == Material.AIR) {
                    kitExtraItems.add(null);
                    continue;
                }
                kitExtraItems.add(originalKitExtraItem.clone());
            }

            kitItems.addAll(kitExtraItems);

            String normal = BukkitSerialization.serializeWithList(kitItems);
            this.plugin.getKitManager().addKit(null, kitName, normal);
        });
    }

    private void savedInventoriesFrom() {
        for (OfflinePlayer offlinePlayer : this.plugin.getServer().getOfflinePlayers()) {
            CMIUser cmiUser = this.cmi.getPlayerManager().getUser(offlinePlayer);
            if (cmiUser == null) continue;

            SavedInventories savedInventories = this.cmi.getSavedInventoryManager().getInventories(cmiUser);
            if (savedInventories == null) continue;

            savedInventories.getInventories().forEach((inventoryId, inventory) -> {
                // Populate a fake full inventory with null this includes armor and offhand. So 0 to 40
                List<ItemStack> items = new ArrayList<>();
                for (int i = 0; i < 41; i++) {
                    items.add(null);
                }

                // Replace null with the actual items.
                inventory.getItems().forEach(items::set);

                String data = BukkitSerialization.serializeWithList(items);
                this.plugin.getSavedInventoriesManager().save(offlinePlayer.getUniqueId(), String.valueOf(inventoryId), data);
            });
        }
    }

    private void homesTo() {
        Map<UUID, Map<String, UnlinkedWorldLocation>> allHomes = this.plugin.getHomeManager().loadAllHomesFromDatabase();

        for (Map.Entry<UUID, Map<String, UnlinkedWorldLocation>> uuidMapEntry : allHomes.entrySet()) {
            UUID uuid = uuidMapEntry.getKey();
            Map<String, UnlinkedWorldLocation> homes = uuidMapEntry.getValue();

            CMIUser cmiUser = this.cmi.getPlayerManager().getUser(uuid);

            homes.forEach((homeName, location) -> cmiUser.addHome(new CmiHome(homeName, new CMILocation(location)), true));
        }
    }

    private void warpsTo() {
        this.plugin.getMemoryHolder().getWarpsMap().forEach((warpName, location) -> {
            CmiWarp cmiWarp = new CmiWarp(warpName, new CMILocation(location));

            // Must set the creator on the warp, or else when /cmi warp is used, it will produce NullPointerException.
            cmiWarp.setCreator(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5")); // Notches UUID

            this.cmi.getWarpManager().addWarp(cmiWarp);
        });
    }

    private void kitsTo() {
        for (KitObject value : this.plugin.getMemoryHolder().getKitsMap().values()) {
            Kit kit = new Kit(value.getKitName());

            List<ItemStack> kitItems = BukkitSerialization.deserializeToList(value.getData());

            List<List<ItemStack>> listList = InventoryUtils.splitInventoryIntoBaseAndExtraContents(kitItems);
            List<ItemStack> baseContents = listList.get(0);
            List<ItemStack> extraContents = listList.get(1);

            // Adjust baseContents for CMI
            List<ItemStack> adjustedBaseContents = CMIDataHelper.adjustItemsForCMIKits(baseContents);

            kit.setItem(adjustedBaseContents);
            CMIDataHelper.setExtraItems(kit, extraContents);

            kit.setDelay(10L);
            kit.setEnabled(true);

            this.cmi.getKitsManager().addKit(kit);
        }

        this.cmi.getKitsManager().safeSave();
    }

    private void savedInventoriesTo() {
        for (OfflinePlayer offlinePlayer : this.plugin.getServer().getOfflinePlayers()) {
            UUID uuid = offlinePlayer.getUniqueId();
            Map<String, SavedInventoryObject> allSavedInventories = this.plugin.getSavedInventoriesManager().load(uuid);

            if (allSavedInventories == null || allSavedInventories.isEmpty()) continue;

            CMIUser cmiUser = this.cmi.getPlayerManager().getUser(offlinePlayer);

            if (cmiUser == null) continue;

            allSavedInventories.forEach((inventoryName, savedInventoryObject) -> {
                List<ItemStack> items = BukkitSerialization.deserializeToList(savedInventoryObject.getData());

                CMIInventory cmiInventory = new CMIInventory(offlinePlayer.getName(), offlinePlayer.getUniqueId());
                cmiInventory.setMaxHealth(20.0D); // Produces IllegalArgumentException if not set lol.

                for (int i = 0; i < items.size(); i++) {
                    ItemStack itemStack = items.get(i);
                    cmiInventory.getItems().put(i, itemStack);
                }

                this.cmi.getSavedInventoryManager().addInventory(cmiUser, cmiInventory);
            });

            this.cmi.getSavedInventoryManager().saveAllInventories(uuid);
        }
    }
}
