package com.andrew121410.mc.world16.events;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16utils.chat.Translate;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Iterator;
import java.util.List;

public class OnServerCommandEvent implements Listener {

    private Main plugin;

    private List<String> spyCommandBlock;

    public OnServerCommandEvent(Main plugin) {
        this.plugin = plugin;

        this.spyCommandBlock = this.plugin.getSetListMap().getSpyCommandBlock();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onCommand(ServerCommandEvent event) {
        if (event.getSender() instanceof BlockCommandSender) {
            BlockCommandSender blockCommandSender = (BlockCommandSender) event.getSender();
            Iterator<String> iterator = spyCommandBlock.iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                if (event.getCommand().contains(s)) {
                    this.plugin.getServer().broadcastMessage(Translate.chat("&cSPY FOUND&e->&r F: " + s + " L: " + blockCommandSender.getBlock().getLocation().toString()));
                    ComponentBuilder components = new ComponentBuilder(Translate.chat("[&eCLICK ME TO TP TO IT EASY&r]")).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + blockCommandSender.getBlock().getLocation().getBlockX() + " " + blockCommandSender.getBlock().getLocation().getBlockY() + " " + blockCommandSender.getBlock().getLocation().getBlockZ()));
                    this.plugin.getServer().spigot().broadcast(components.create());
                    iterator.remove();
                }
            }
        }
    }
}
