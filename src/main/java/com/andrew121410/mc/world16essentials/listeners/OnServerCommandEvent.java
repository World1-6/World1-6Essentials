package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16utils.chat.Translate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Iterator;
import java.util.List;

public class OnServerCommandEvent implements Listener {

    private final World16Essentials plugin;
    private final List<String> spyCommandBlock;

    public OnServerCommandEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.spyCommandBlock = this.plugin.getMemoryHolder().getSpyCommandBlock();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onCommand(ServerCommandEvent event) {
        // Don't try to iterate if the list is empty.
        if (spyCommandBlock.isEmpty()) return;

        if (event.getSender() instanceof BlockCommandSender blockCommandSender) {
            Iterator<String> iterator = spyCommandBlock.iterator();
            while (iterator.hasNext()) {
                String string = iterator.next();
                if (event.getCommand().contains(string)) {
                    this.plugin.getServer().broadcast(Translate.miniMessage("<gold>CommandBlockSpy: <gray>X: " + blockCommandSender.getBlock().getX() + " Y: " + blockCommandSender.getBlock().getY() + " Z: " + blockCommandSender.getBlock().getZ() + " <gold>Found: <dark_green>" + event.getCommand()));
                    TextComponent.Builder text = Component.text().append(Translate.miniMessage("<gold><bold>Click to teleport to command block")).clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/tp " + blockCommandSender.getBlock().getX() + " " + blockCommandSender.getBlock().getY() + " " + blockCommandSender.getBlock().getZ())).hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(Translate.miniMessage("<gold><bold>Click to teleport to command block")));
                    this.plugin.getServer().broadcast(text.build());
                    iterator.remove();
                }
            }
        }
    }
}
