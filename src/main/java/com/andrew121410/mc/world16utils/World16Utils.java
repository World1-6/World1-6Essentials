package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16utils.chat.ChatClickCallbackManager;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.config.UnlinkedWorldLocation;
import com.andrew121410.mc.world16utils.listeners.OnAsyncPlayerChatEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;
import com.andrew121410.mc.world16utils.listeners.OnPlayerQuitEvent;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.function.Consumer;

public final class World16Utils extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(UnlinkedWorldLocation.class, "UnlinkedWorldLocation");
    }

    public static final String DATE_OF_VERSION = "7/26/2022";
    public static final String PREFIX = "[&9World1-6Utils&r]";
    private static World16Utils instance;

    private ChatResponseManager chatResponseManager;
    private ChatClickCallbackManager chatClickCallbackManager;

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        instance = this;
        this.chatResponseManager = new ChatResponseManager(this);
        this.chatClickCallbackManager = new ChatClickCallbackManager(this);
        registerListeners();
        registerCommand();
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners() {
        new OnAsyncPlayerChatEvent(this, this.chatResponseManager);
        new OnInventoryClickEvent(this);
        new OnInventoryCloseEvent(this);
        new OnPlayerQuitEvent(this);
    }

    private void registerCommand() {
        getCommand("world1-6utils").setExecutor((sender, command, s, args) -> {
            if (!sender.hasPermission("world16.world1-6utils")) {
                sender.sendMessage("You do not have permission to use this command.");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("/world1-6utils update");
            } else if (args[0].equalsIgnoreCase("update") && args.length == 2) {
                new UnsupportedOperationException("Update command is not supported.");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("callclickevent")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("You must be a player to use this command.");
                    return true;
                }

                Map<String, Consumer<Player>> map = this.chatClickCallbackManager.getMap().get(player.getUniqueId());
                if (map == null) return true;

                String key = args[1];
                Consumer<Player> consumer = map.get(key);
                if (consumer == null) return true;
                consumer.accept((Player) sender);
                map.remove(key);
            }
            return true;
        });
    }

    public static World16Utils getInstance() {
        return instance;
    }

    public ChatResponseManager getChatResponseManager() {
        return chatResponseManager;
    }

    public ChatClickCallbackManager getChatClickCallbackManager() {
        return chatClickCallbackManager;
    }
}
