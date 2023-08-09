package com.andrew121410.mc.world16utils.player;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class PlayerUtils {

    public static final ConcurrentHashMap<UUID, GameProfile> PLAYER_PROFILES_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();
    public static final ExecutorService PROFILE_EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

    public static boolean smoothTeleport(Player player, Location location) {
        return player.teleport(location);
    }

    public static Block getBlockPlayerIsLookingAt(Player player) {
        return player.getTargetBlock(null, 5);
    }

    public static Block getBlockPlayerIsLookingAt(Player player, int range) {
        return player.getTargetBlock(null, range);
    }

    public static CompletableFuture<ItemStack> getPlayerHead(OfflinePlayer player) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        // Set the display name
        if (player.getName() != null) {
            skullMeta.setDisplayName(player.getName());
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        GameProfile gameProfile = craftPlayer.getProfile();
        if (gameProfile != null) {
            setSkullProfile(skullMeta, gameProfile);
            itemStack.setItemMeta(skullMeta);
            return CompletableFuture.completedFuture(itemStack);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                Field profileField = CraftPlayer.class.getDeclaredField("playerProfile");
                profileField.setAccessible(true);
                GameProfile newProfile = (GameProfile) profileField.get(craftPlayer);

                PLAYER_PROFILES_CONCURRENT_HASH_MAP.putIfAbsent(player.getUniqueId(), newProfile);
                setSkullProfile(skullMeta, newProfile);
                itemStack.setItemMeta(skullMeta);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemStack;
        }, PROFILE_EXECUTOR_SERVICE);
    }

    private static void setSkullProfile(SkullMeta skullMeta, GameProfile gameProfile) {
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPlayerHead(OfflinePlayer player, Consumer<ItemStack> consumer) {
        getPlayerHead(player).thenAcceptAsync(consumer, runnable -> Bukkit.getScheduler().runTask(World16Essentials.getPlugin(), runnable));
    }

    public static void getPlayerHeads(List<OfflinePlayer> players, Consumer<Map<OfflinePlayer, ItemStack>> consumer) {
        Map<OfflinePlayer, CompletableFuture<ItemStack>> completableFutures = new HashMap<>();
        for (OfflinePlayer player : players) {
            completableFutures.put(player, getPlayerHead(player));
        }

        Map<OfflinePlayer, ItemStack> itemStacks = new HashMap<>();
        CompletableFuture.allOf(completableFutures.values().toArray(new CompletableFuture[0])).thenAcceptAsync((v) -> {
            for (Map.Entry<OfflinePlayer, CompletableFuture<ItemStack>> entry : completableFutures.entrySet()) {
                OfflinePlayer player = entry.getKey();
                CompletableFuture<ItemStack> completableFuture = entry.getValue();
                try {
                    ItemStack itemStack = completableFuture.get();
                    itemStacks.put(player, itemStack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            consumer.accept(itemStacks);
        }, runnable -> Bukkit.getScheduler().runTask(World16Essentials.getPlugin(), runnable));
    }
}
