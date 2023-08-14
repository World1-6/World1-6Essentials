package com.andrew121410.mc.world16utils.chat;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.World16Utils;
import com.destroystokyo.paper.Title;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ChatResponseManager {

    private final World16Essentials plugin;
    private boolean running;

    private final Map<UUID, Response> responseMap;

    public ChatResponseManager(World16Essentials plugin) {
        this.plugin = plugin;
        this.responseMap = new HashMap<>();
    }

    public boolean create(Player player, BiConsumer<Player, String> consumer) {
        return create(player, (String) null, null, consumer);
    }

    public boolean create(Player player, String title, String subtitle, BiConsumer<Player, String> consumer) {
        if (this.responseMap.containsKey(player.getUniqueId())) return false;
        this.responseMap.put(player.getUniqueId(), new Response(title, subtitle, consumer));
        loop();
        return true;
    }

    public boolean isWaiting(UUID uuid) {
        return this.responseMap.containsKey(uuid);
    }

    public BiConsumer<Player, String> get(Player player) {
        if (!this.responseMap.containsKey(player.getUniqueId())) return null;
        return this.responseMap.get(player.getUniqueId()).getConsumer();
    }

    public void remove(UUID uuid) {
        this.responseMap.remove(uuid);
    }

    private void loop() {
        if (this.running) return;
        this.running = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (responseMap.size() == 0) {
                    running = false;
                    this.cancel();
                }
                Iterator<Map.Entry<UUID, Response>> iterator = responseMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<UUID, Response> entry = iterator.next();
                    UUID uuid = entry.getKey();
                    Response response = entry.getValue();

                    Player player = World16Essentials.getPlugin().getServer().getPlayer(uuid);
                    if (player == null) {
                        iterator.remove();
                        return;
                    }

                    String title = response.getTitle() != null ? response.getTitle() : Translate.color("&bType in response");
                    String subtitle = response.getSubtile() != null ? response.getSubtile() : "";

                    player.sendTitle(new Title(title, subtitle, 0, 61, 0));

                    player.sendActionBar(Translate.color("&eType &ccancel &eto stop!"));
                }
            }
        }.runTaskTimer(World16Essentials.getPlugin(), 0L, 60L);
    }
}

class Response {
    String title;
    String subtile;
    BiConsumer<Player, String> consumer;

    public Response(String title, String subtile, BiConsumer<Player, String> consumer) {
        this.title = title;
        this.subtile = subtile;
        this.consumer = consumer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtile() {
        return subtile;
    }

    public void setSubtile(String subtile) {
        this.subtile = subtile;
    }

    public BiConsumer<Player, String> getConsumer() {
        return consumer;
    }

    public void setConsumer(BiConsumer<Player, String> consumer) {
        this.consumer = consumer;
    }
}

