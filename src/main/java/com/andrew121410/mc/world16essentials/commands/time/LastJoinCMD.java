package com.andrew121410.mc.world16essentials.commands.time;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.GUIMultipageListWindow;
import com.andrew121410.mc.world16utils.gui.buttons.GUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.GUIClickEvent;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LastJoinCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

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

        if (!player.hasPermission("world16.lastjoin")) {
            this.plugin.getApi().sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            new GUIMultipageListWindow("Last Join", 54, makeGUIButtons(player), 45).open(player);
            return true;
        } else if (args.length == 1) {
            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(args[0]);

            if (!offlinePlayer.hasPlayedBefore()) {
                player.sendMessage(Translate.chat("&cThis user has never joined before..."));
                return true;
            }

            player.sendMessage(Translate.chat("&aLast join of &6" + offlinePlayer.getName() + "&a was &6" + this.api.getTimeSinceLastLogin(offlinePlayer)));
        }
        return true;
    }

    private List<GUIButton> makeGUIButtons(Player player) {
        OfflinePlayer[] offlinePlayers = this.plugin.getServer().getOfflinePlayers();

        List<LastJoinGUIButton> guiButtons = new ArrayList<>();
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            if (offlinePlayer == null || offlinePlayer.getName() == null) continue;

            guiButtons.add(new LastJoinGUIButton(offlinePlayer.getLastPlayed(), -1, InventoryUtils.createItem(Material.PLAYER_HEAD, 1, offlinePlayer.getName(), api.getTimeSinceLastLogin(offlinePlayer)), (guiClickEvent) -> {
                player.sendMessage(Translate.chat("&aLast join of &6" + offlinePlayer.getName() + "&a was &6" + this.api.getTimeSinceLastLogin(offlinePlayer)));
            }));
        }

        sortByLeastToGreatestTime(guiButtons);

        return new ArrayList<>(guiButtons);
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
        if (o == null || getClass() != o.getClass()) return false;

        LastJoinGUIButton that = (LastJoinGUIButton) o;

        if (lastTimePlayed != that.lastTimePlayed) return false;
        return itemStack != null ? itemStack.equals(that.itemStack) : that.itemStack == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (lastTimePlayed ^ (lastTimePlayed >>> 32));
        result = 31 * result + (itemStack != null ? itemStack.hashCode() : 0);
        return result;
    }
}
