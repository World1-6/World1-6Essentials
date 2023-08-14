package com.andrew121410.mc.world16utils;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.listeners.OnAsyncPlayerChatEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryClickEvent;
import com.andrew121410.mc.world16utils.listeners.OnInventoryCloseEvent;
import com.andrew121410.mc.world16utils.listeners.OnPlayerQuitEvent;

public class World16Utils {

    private static World16Utils INSTANCE;

    private ChatResponseManager chatResponseManager;

    public void onEnable(World16Essentials plugin) {
        INSTANCE = this;

        // Managers
        this.chatResponseManager = new ChatResponseManager(plugin);

        // Register listeners
        new OnAsyncPlayerChatEvent(plugin, chatResponseManager);
        new OnInventoryClickEvent(plugin);
        new OnInventoryCloseEvent(plugin);
        new OnPlayerQuitEvent(this, plugin);
    }

    public ChatResponseManager getChatResponseManager() {
        return chatResponseManager;
    }

    public static World16Utils getInstance() {
        return INSTANCE;
    }
}
