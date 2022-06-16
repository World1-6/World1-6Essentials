package com.andrew121410.mc.world16essentials.commands.time;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.AdvanceGUIWindow;
import com.andrew121410.mc.world16utils.gui.buttons.GUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.GUIClickEvent;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.Utils;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
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
            openInventory(player);
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

    private void openInventory(Player player) {
        AdvanceGUIWindow window = new AdvanceGUIWindow() {

            private int page = 0;

            @Override
            public void onCreate(Player player) {
                List<List<LastJoinGUIButton>> pages = Lists.partition(makeGUIButtons(player), 45);
                List<GUIButton> bottomButtons = new ArrayList<>();

                if (page != 0) {
                    bottomButtons.add(new ClickEventButton(45, InventoryUtils.createItem(Material.ARROW, 1, "Previous Page"), (guiClickEvent) -> {
                        if (Utils.indexExists(pages, page - 1)) {
                            page--;
                            List<GUIButton> guiButtonList = new ArrayList<>(pages.get(page));
                            guiButtonList.addAll(bottomButtons);
                            player.closeInventory();
                            this.update(guiButtonList, "Last Join", 54);
                            this.open(player);
                        }
                    }));
                }

                if (pages.size() >= 2) {
                    bottomButtons.add(new ClickEventButton(53, InventoryUtils.createItem(Material.ARROW, 1, "Go to next page"), (guiClickEvent) -> {
                        if (Utils.indexExists(pages, page + 1)) {
                            page++;
                            List<GUIButton> guiButtonList = new ArrayList<>(pages.get(page));
                            guiButtonList.addAll(bottomButtons);
                            player.closeInventory();
                            this.update(guiButtonList, "Last Join", 54);
                            this.open(player);
                        }
                    }));
                }

                if (page == 0) {
                    List<GUIButton> guiButtonList = new ArrayList<>(pages.get(0));
                    guiButtonList.addAll(bottomButtons);
                    this.update(guiButtonList, "Last Join", 54);
                }
            }

            @Override
            public void onClose(InventoryCloseEvent inventoryCloseEvent) {

            }
        };

        window.open(player);
    }

    private List<LastJoinGUIButton> makeGUIButtons(Player player) {
        OfflinePlayer[] offlinePlayers = this.plugin.getServer().getOfflinePlayers();

        List<LastJoinGUIButton> guiButtons = new ArrayList<>();
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            guiButtons.add(new LastJoinGUIButton(offlinePlayer.getLastPlayed(), 0, InventoryUtils.createItem(Material.PLAYER_HEAD, 1, offlinePlayer.getName(), api.getTimeSinceLastLogin(offlinePlayer)), (guiClickEvent) -> {
                player.sendMessage(Translate.chat("&aLast join of &6" + offlinePlayer.getName() + "&a was &6" + this.api.getTimeSinceLastLogin(offlinePlayer)));
            }));
        }

        setToTheRealSlots(guiButtons);
        sortByLeastToGreatestTime(guiButtons);

        return guiButtons;
    }

    private void setToTheRealSlots(List<LastJoinGUIButton> guiButtonList) {
        int i = -1;
        int max = 45;

        for (GUIButton guiButton : guiButtonList) {
            if (i < max) {
                i++;
            } else {
                i = 0;
            }
            guiButton.setSlot(i);
        }
    }

    private void sortByLeastToGreatestTime(List<LastJoinGUIButton> guiButtons) {
        guiButtons.sort(Comparator.comparingLong(LastJoinGUIButton::getLastTimePlayed));
    }
}

class LastJoinGUIButton extends ClickEventButton {

    private final long lastTimePlayed;

    public LastJoinGUIButton(Long lastTimePlayed, int slot, ItemStack itemStack, Consumer<GUIClickEvent> consumer) {
        super(slot, itemStack, consumer);
        this.lastTimePlayed = lastTimePlayed;
    }

    public long getLastTimePlayed() {
        return lastTimePlayed;
    }
}
