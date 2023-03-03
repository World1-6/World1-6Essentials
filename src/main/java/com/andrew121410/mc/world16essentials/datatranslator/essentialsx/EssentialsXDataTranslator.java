package com.andrew121410.mc.world16essentials.datatranslator.essentialsx;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.datatranslator.DataTranslator;
import com.andrew121410.mc.world16essentials.datatranslator.IDataTranslator;
import com.andrew121410.mc.world16essentials.objects.SavedInventoryObject;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.utils.BukkitSerialization;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

// As of 1/3/2023 this still works. Tested on EssentialsX 2.20.0-dev+37-b7a4bea
public class EssentialsXDataTranslator implements IDataTranslator {

    private final World16Essentials plugin;
    private final Essentials essentials;

    public EssentialsXDataTranslator(World16Essentials plugin, Essentials essentials) {
        this.plugin = plugin;
        this.essentials = essentials;
    }

    @Override
    public boolean convertFrom(Player player) {
        this.homesFrom();
        this.warpsFrom();
        this.kitsFrom(player);
        return true;
    }

    @Override
    public boolean convertTo(Player player) {
        this.homesTo();
        this.warpsTo();
        this.kitsTo();
        return true;
    }

    private void homesFrom() {
        if (hasEssentialsConfigFolder() == null) return;

        List<File> userDataFiles = getAllUserDataYmlFiles();
        for (File userDataFile : userDataFiles) {
            String stringUUID = userDataFile.getName().replace(".yml", "");
            UUID uuid = UUID.fromString(stringUUID);

            FileConfiguration config = DataTranslator.loadConfig(userDataFile);
            ConfigurationSection homesSelection = config.getConfigurationSection("homes");
            if (homesSelection == null) {
                // User has no homes
                continue;
            }
            for (String homeName : homesSelection.getKeys(false)) {
                ConfigurationSection homeSelection = homesSelection.getConfigurationSection(homeName);
                UUID worldUUID = UUID.fromString(homeSelection.getString("world"));
                double x = homeSelection.getDouble("x");
                double y = homeSelection.getDouble("y");
                double z = homeSelection.getDouble("z");
                float yaw = (float) homeSelection.getDouble("yaw");
                float pitch = (float) homeSelection.getDouble("pitch");
                Location location = new Location(Bukkit.getWorld(worldUUID), x, y, z, yaw, pitch);

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                this.plugin.getHomeManager().add(offlinePlayer, homeName, new UnlinkedWorldLocation(location));
            }
        }
    }

    private void homesTo() {
        // Load all homes including offline players
        Map<UUID, Map<String, UnlinkedWorldLocation>> allHomes = this.plugin.getHomeManager().loadAllHomesFromDatabase();

        for (Map.Entry<UUID, Map<String, UnlinkedWorldLocation>> uuidMapEntry : allHomes.entrySet()) {
            UUID uuid = uuidMapEntry.getKey();
            Map<String, UnlinkedWorldLocation> homes = uuidMapEntry.getValue();
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

            User user = offlinePlayer.isOnline() ? this.essentials.getUser(uuid) : this.essentials.getOfflineUser(offlinePlayer.getName());
            if (user == null) {
                // https://discord.com/channels/390942438061113344/395339753375137820/1078920593543528569
                ImporterOfflinePlayer importerOfflinePlayer = new ImporterOfflinePlayer(offlinePlayer.getName(), uuid, Bukkit.getServer());
                user = new User(importerOfflinePlayer, this.essentials);
                user.save();
            }

            for (Map.Entry<String, UnlinkedWorldLocation> entry : homes.entrySet()) {
                String homeName = entry.getKey();
                UnlinkedWorldLocation location = entry.getValue();

                user.setHome(homeName, location);
            }
        }
    }

    private void warpsFrom() {
        for (String warpName : essentials.getWarps().getList()) {
            Location location = null;
            try {
                location = essentials.getWarps().getWarp(warpName);
            } catch (Exception ignored) {

            }

            if (location != null) {
                this.plugin.getWarpManager().add(warpName, location);
            }
        }
    }

    private void warpsTo() {
        this.plugin.getSetListMap().getWarpsMap().forEach((warpName, location) -> {
            try {
                essentials.getWarps().setWarp(warpName, location);
            } catch (Exception ignored) {
            }
        });
    }

    private void kitsFrom(Player player) {
        player.sendMessage(Translate.miniMessage("<gold>Starting kits conversion..."));

        // Create a save of the inventory
        String saveInventoryName = "temp-" + UUID.randomUUID();
        SavedInventoryObject savedInventoryObject = SavedInventoryObject.create(player, saveInventoryName);

        // Convert all kits
        for (String kitKey : this.essentials.getKits().getKitKeys()) {
            player.getInventory().clear();
            Bukkit.getServer().dispatchCommand(player, "essentials:kit " + kitKey);
            this.plugin.getKitManager().addKit(player.getUniqueId(), kitKey, BukkitSerialization.turnInventoryIntoBase64s(player));
            player.sendMessage(Translate.miniMessage("<green>Translated kit: <yellow>" + kitKey));
        }

        // Restore their inventory
        player.getInventory().clear();
        savedInventoryObject.give(player);
        player.sendMessage(Translate.miniMessage("<green><bold>Finished kits conversion!"));
    }

    private void kitsTo() {
        this.plugin.getSetListMap().getKitsMap().forEach((s, kit) -> {
            ItemStack[] regularItems;
            try {
                regularItems = BukkitSerialization.base64ToItemStackArray(kit.getData()[0]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<String> data = new ArrayList<>();

            // Just took this from EssentialsX
            ItemStack[] var11 = regularItems;
            int var12 = regularItems.length;
            for (int var13 = 0; var13 < var12; ++var13) {
                ItemStack is = var11[var13];
                if (is != null && is.getType() != null && is.getType() != Material.AIR) {
                    String serialized = "@" + Base64Coder.encodeLines(this.essentials.getSerializationProvider().serializeItem(is));
                    data.add(serialized);
                }
            }
            this.essentials.getKits().addKit(kit.getKitName(), data, 1);
        });
    }

    private File hasEssentialsConfigFolder() {
        File file = new File("plugins/Essentials/");
        return file.exists() ? file : null;
    }

    private List<File> getAllUserDataYmlFiles() {
        if (hasEssentialsConfigFolder() == null) return null;
        File userDataFolder = new File(hasEssentialsConfigFolder(), "userdata/");
        return Arrays.stream(Objects.requireNonNull(userDataFolder.listFiles())).filter(file -> file.getName().endsWith(".yml")).collect(Collectors.toList());
    }
}