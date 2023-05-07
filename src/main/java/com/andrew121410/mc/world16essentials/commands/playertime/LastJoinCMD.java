package com.andrew121410.mc.world16essentials.commands.playertime;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.GUIMultipageListWindow;
import com.andrew121410.mc.world16utils.gui.buttons.CloneableGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.events.GUIClickEvent;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class LastJoinCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    private boolean isFirst = true;

    public LastJoinCMD(World16Essentials plugin) {
        this.plugin = plugin;
        this.api = this.plugin.getApi();

        this.plugin.getCommand("lastjoin").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.lastjoin") && !player.hasPermission("world16.lastonline")) {
            this.plugin.getApi().sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            if (this.isFirst) {
                player.sendMessage(Translate.color("&ePlease wait, this may take a while..."));
                this.isFirst = false;
            }
            makeGUIButtons(player, guiButtonList -> {
                GUIMultipageListWindow gui = new GUIMultipageListWindow(Translate.miniMessage("<gold>Last Join's"), guiButtonList);
                gui.setPageEvent(guiNextPageEvent -> {
                    if (guiNextPageEvent.isAfterPageCreation()) return;
                    player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
                });
                gui.open(player);
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1f, 1f);
            });
            return true;
        } else if (args.length == 1) {
            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[0]);

            if (!offlinePlayer.hasPlayedBefore()) {
                player.sendMessage(Translate.color("&cThis user has never joined before..."));
                return true;
            }

            player.sendMessage(Translate.color("&aLast join of &6" + offlinePlayer.getName() + "&a was &6" + this.api.getTimeSinceLastLogin(offlinePlayer) + " &aago."));
        }
        return true;
    }

    private void makeGUIButtons(Player player, Consumer<List<CloneableGUIButton>> callback) {
        OfflinePlayer[] offlinePlayers = this.plugin.getServer().getOfflinePlayers();

        PlayerUtils.getPlayerHeads(List.of(offlinePlayers), (map) -> {
            List<LastJoinGUIButton> guiButtons = new ArrayList<>();

            map.forEach((offlinePlayer, itemStack) -> {
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(Collections.singletonList(api.getTimeSinceLastLogin(offlinePlayer)));
                itemStack.setItemMeta(itemMeta);

                guiButtons.add(new LastJoinGUIButton(offlinePlayer.getLastLogin(), -1, itemStack, (guiClickEvent) -> {
                    player.sendMessage(Translate.color("&aLast join of &6" + offlinePlayer.getName() + "&a was &6" + this.api.getTimeSinceLastLogin(offlinePlayer) + " &aago."));
                }));
            });

            sortByLeastToGreatestTime(guiButtons);

            callback.accept(new ArrayList<>(guiButtons));
        });
    }

    private void sortByLeastToGreatestTime(List<LastJoinGUIButton> guiButtons) {
        guiButtons.sort(((o1, o2) -> Long.compare(o2.getLastTimePlayed(), o1.getLastTimePlayed())));
    }
}

class LastJoinGUIButton extends ClickEventButton {

    private final long lastTimePlayed;
    private final ItemStack itemStack;

    public LastJoinGUIButton(Long lastTimePlayed, int slot, ItemStack itemStack, Consumer<GUIClickEvent> consumer) {
        super(slot, itemStack, consumer);
        this.lastTimePlayed = lastTimePlayed;
        this.itemStack = itemStack;
    }

    public long getLastTimePlayed() {
        return lastTimePlayed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LastJoinGUIButton that)) return false;
        if (!super.equals(o)) return false;

        if (lastTimePlayed != that.lastTimePlayed) return false;
        return itemStack != null ? itemStack.equals(that.itemStack) : that.itemStack == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (lastTimePlayed ^ (lastTimePlayed >>> 32));
        result = 31 * result + (itemStack != null ? itemStack.hashCode() : 0);
        return result;
    }
}
