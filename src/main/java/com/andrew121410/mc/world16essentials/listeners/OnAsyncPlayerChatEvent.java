package com.andrew121410.mc.world16essentials.listeners;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class OnAsyncPlayerChatEvent implements Listener {

    private World16Essentials plugin;
    private API api;

    private List<Player> hiddenPlayers;

    public OnAsyncPlayerChatEvent(World16Essentials plugin) {
        this.plugin = plugin;
        this.hiddenPlayers = this.plugin.getSetListMap().getHiddenPlayers();
        this.api = new API(this.plugin);

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void ChatEvent(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        String cmd = event.getMessage();

        //Name ping
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    if (cmd.contains(player.getDisplayName())) {
                        if (api.isAfk(player)) {
                            plugin.getServer().broadcastMessage(Translate.chat("&6[Afk] &eLooks like &r&9" + player.getDisplayName() + "&r&e, is afk they may not respond."));
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                    }

                    if (cmd.contains("!" + player.getDisplayName())) {
                        player.playSound(player.getLocation(), Sound.ENTITY_HORSE_DEATH, 10.0f, 1.0f);
                    }
                }
            }
        }.runTask(this.plugin);

        //Don't run if the first part isn't :
        if (!cmd.startsWith(":")) {
            return;
        }

        String[] args = cmd.split(" ");

        if (!p.isOp()) {
            return;
        }

        if (args[0].equalsIgnoreCase(":help")) {
            event.setCancelled(true);
            p.sendMessage(Translate.chat("&6-----&c[Secret Commands]&r&6-----"));
            p.sendMessage(Translate.chat("&6:msg OR :emsg [SAME THING AS /emsg]"));
            p.sendMessage(Translate.chat("&6:tp <User> {Makes you hidden and then tp's to player.}"));
            p.sendMessage(Translate.chat("&6:unhide [It unhides you.]"));
            p.sendMessage(Translate.chat("&6:hide [Hides you from server.]"));
            p.sendMessage(Translate.chat("&6:list [List of all of the hidden players.]"));
            p.sendMessage(Translate.chat("&6:bye [Fake leave message]"));
            return;
        }

        if (args[0].equalsIgnoreCase(":msg") || args[0].equalsIgnoreCase(":emsg")) {
            event.setCancelled(true);
            if (!p.hasPermission("world16.msg")) {
                api.permissionErrorMessage(p);
                return;
            }
            if (args.length == 1) {
                p.sendMessage(Translate.chat("&cUsage: :msg <Player> <Message>"));
            } else if (args.length >= 3) {
                Player ptarget = this.plugin.getServer().getPlayerExact(args[1]);
                if (args[1] != null && args[2] != null && ptarget != null && ptarget.isOnline()) {
                    String messageFrom = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                    p.sendMessage(Translate.chat("&2[&a{me} &6->&c {target}&2]&9 ->&r {message}").replace("{me}", "me").replace("{target}", ptarget.getDisplayName()).replace("{message}", messageFrom));
                    ptarget.sendMessage(Translate.chat("&2[&a{me} &6->&c {target}&2]&9 ->&r {message}").replace("{me}", p.getDisplayName()).replace("{target}", "me").replace("{message}", messageFrom));
                } else {
                    p.sendMessage(Translate.chat("&4Something went wrong."));
                    p.sendMessage(Translate.chat("-> &cUsage: :msg <Player> <Message>"));
                }
            } else {
                p.sendMessage(Translate.chat("&4Something went wrong."));
                p.sendMessage(Translate.chat("-> &cUsage: :msg <Player> <Message>"));
            }
        }
        if (args[0].equalsIgnoreCase(":tp")) {
            event.setCancelled(true);
            if (!p.hasPermission("world16.tp")) {
                api.permissionErrorMessage(p);
                return;
            }
            if (args.length == 1) {
                p.sendMessage(Translate.chat("&cUsage: :tp <Player>"));
            } else if (args.length == 2) {
                Player pTarget = this.plugin.getServer().getPlayerExact(args[1]);
                if (args[1] != null && pTarget != null && pTarget.isOnline()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            hiddenPlayers.add(p);
                            plugin.getServer().getOnlinePlayers().forEach(player -> player.hidePlayer(p));
                            if (!pTarget.canSee(p)) {
                                p.teleport(pTarget.getLocation());
                                p.sendMessage(Translate.chat("&bOk..."));
                                p.sendMessage(Translate.chat("Too unhide use :unhide"));
                            }
                        }
                    }.runTask(this.plugin);
                }
            }
        }
        if (args[0].equalsIgnoreCase(":unhide")) {
            event.setCancelled(true);
            if (!p.hasPermission("world16.unhide")) {
                api.permissionErrorMessage(p);
                return;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    hiddenPlayers.remove(p);
                    plugin.getServer().getOnlinePlayers().forEach(player -> player.showPlayer(p));
                    p.sendMessage(Translate.chat("&bOk..."));
                }
            }.runTask(this.plugin);
        }
        if (args[0].equalsIgnoreCase(":hide")) {
            event.setCancelled(true);
            if (!p.hasPermission("world16.hide")) {
                api.permissionErrorMessage(p);
                return;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    hiddenPlayers.add(p);
                    plugin.getServer().getOnlinePlayers().forEach(player -> player.hidePlayer(p));
                    p.sendMessage(Translate.chat("&bOk..."));
                }
            }.runTask(this.plugin);
        }

        if (args[0].equalsIgnoreCase(":list")) {
            event.setCancelled(true);
            for (Player player : hiddenPlayers) {
                p.sendMessage("This player is hidden -> " + player.getDisplayName());
            }
        }

        if (args[0].equalsIgnoreCase(":bye")) {
            event.setCancelled(true);
            Bukkit.broadcastMessage(Translate.chat(API.PREFIX + " &5Bye Bye, " + p.getDisplayName()));
        }
    }
}