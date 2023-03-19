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

import java.io.IOException;
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
            List<ItemStack> kitItems = kit.getItems();
            List<ItemStack> extraItems = CMIDataHelper.getExtraItems(kit);

            // Reverse the list
            Collections.reverse(kitItems);

            kitItems.addAll(extraItems);

            String normal = BukkitSerialization.itemStackArrayToBase64(kitItems.toArray(new ItemStack[0]));
            String armor = BukkitSerialization.itemStackArrayToBase64(extraItems.toArray(new ItemStack[0]));
            this.plugin.getKitManager().addKit(null, kitName, new String[]{normal, armor});
        });
    }

    private void savedInventoriesFrom() {
        for (OfflinePlayer offlinePlayer : this.plugin.getServer().getOfflinePlayers()) {
            CMIUser cmiUser = this.cmi.getPlayerManager().getUser(offlinePlayer);
            if (cmiUser == null) continue;

            SavedInventories savedInventories = this.cmi.getSavedInventoryManager().getInventories(cmiUser);
            if (savedInventories == null) continue;

            savedInventories.getInventories().forEach((inventoryId, inventory) -> {
                String[] data = new String[]{
                        BukkitSerialization.itemStackArrayToBase64(inventory.getItems().values().toArray(new ItemStack[0])),
                        BukkitSerialization.itemStackArrayToBase64(new ItemStack[]{new ItemStack(Material.AIR)})};

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

            ItemStack[] itemStacks;
            try {
                itemStacks = BukkitSerialization.base64ToItemStackArray(value.getData()[0]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<ItemStack> kitItems = new ArrayList<>(Arrays.asList(itemStacks));
            List<List<ItemStack>> inventory = InventoryUtils.splitInventoryIntoBaseAndExtraContents(kitItems);
            List<ItemStack> baseContents = inventory.get(0);
            List<ItemStack> extraContents = inventory.get(1);

            kit.setItem(baseContents);
            CMIDataHelper.setExtraItems(kit, extraContents);

            kit.setDelay(10L);
            kit.setEnabled(true);

            this.cmi.getKitsManager().addKit(kit);
        }
    }

    private void savedInventoriesTo() {
        for (OfflinePlayer offlinePlayer : this.plugin.getServer().getOfflinePlayers()) {
            UUID uuid = offlinePlayer.getUniqueId();
            Map<String, SavedInventoryObject> allSavedInventories = this.plugin.getSavedInventoriesManager().load(uuid);

            if (allSavedInventories == null || allSavedInventories.isEmpty()) continue;

            CMIUser cmiUser = this.cmi.getPlayerManager().getUser(offlinePlayer);

            if (cmiUser == null) continue;

            allSavedInventories.forEach((inventoryName, savedInventoryObject) -> {
                String[] data = savedInventoryObject.getData();
                ItemStack[] items;
                ItemStack[] armor;
                try {
                    items = BukkitSerialization.base64ToItemStackArray(data[0]);
                    armor = BukkitSerialization.base64ToItemStackArray(data[1]);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                CMIInventory cmiInventory = new CMIInventory(offlinePlayer.getName(), offlinePlayer.getUniqueId());
                cmiInventory.setMaxHealth(20.0D); // Produces IllegalArgumentException if not set lol.

                List<ItemStack> itemsList = new ArrayList<>(Arrays.asList(items));
                itemsList.addAll(List.of(armor));

                for (int i = 0; i < itemsList.size(); i++) {
                    ItemStack itemStack = itemsList.get(i);
                    cmiInventory.getItems().put(i, itemStack);
                }

                this.cmi.getSavedInventoryManager().addInventory(cmiUser, cmiInventory);
            });
        }
    }
}
