package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.ChatClickCallbackManager;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.GUIWindow;
import com.andrew121410.mc.world16utils.gui.buttons.AbstractGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.LoreShifterButton;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.TabUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public SignCMD(World16Essentials plugin) {
        this.plugin = plugin;

        this.api = this.plugin.getApi();
        this.plugin.getCommand("sign").setExecutor(this);
        this.plugin.getCommand("sign").setTabCompleter((commandSender, command, s, args) -> {
            if (args.length == 1)
                return TabUtils.getContainsString(args[0], Arrays.asList("give", "edit", "edit-legacy"));
            if (args.length == 2 && args[0].equalsIgnoreCase("edit"))
                return TabUtils.getContainsString(args[1], Arrays.asList("@regular", "@minimessage"));
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        if (!player.hasPermission("world16.sign")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("give")) {
            ItemStack itemStack = new ItemStack(Material.OAK_SIGN, 1);
            itemStack.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            player.getInventory().addItem(itemStack);
            return true;
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("edit")) {
            Block block = PlayerUtils.getBlockPlayerIsLookingAt(player);
            BlockState state = block.getState();

            if (!(state instanceof Sign sign)) {
                player.sendMessage(Translate.colorc("&4Please look at a sign."));
                return true;
            }

            if (args.length == 2 && args[1].equalsIgnoreCase("@minimessage")) {
                editGUI(player, sign, false);
            } else {
                editGUI(player, sign, true);
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("edit-legacy")) {
            Block block = PlayerUtils.getBlockPlayerIsLookingAt(player);
            BlockState state = block.getState();

            if (!(state instanceof Sign sign)) {
                player.sendMessage(Translate.colorc("&4Please look at a sign."));
                return true;
            }

            @NotNull
            Side side = sign.getInteractableSideFor(player);
            player.openSign(sign, side);
            return true;
        } else {
            player.sendMessage(Translate.colorc("&cUsage: /sign <give|edit|edit-legacy>"));
        }
        return true;
    }

    private void editGUI(Player player, Sign sign, boolean isRegular) { // isRegular = true = regular old &, false = minimessage
        ChatResponseManager chatResponseManager = this.plugin.getOtherPlugins().getWorld16Utils().getChatResponseManager();
        ChatClickCallbackManager chatClickCallbackManager = this.plugin.getOtherPlugins().getWorld16Utils().getChatClickCallbackManager();
        GUIWindow guiWindow = new GUIWindow() {
            @Override
            public void onCreate(Player player) {
                List<AbstractGUIButton> guiButtons = new ArrayList<>();

                SignSide signSide = sign.getTargetSide(player);

                for (int i = 0; i < signSide.lines().size(); i++) {
                    int finalI = i;
                    Component signLineComponent = signSide.line(finalI);
                    String currentLineFormatted = isRegular ? LegacyComponentSerializer.legacyAmpersand().serialize(signLineComponent) : MiniMessage.miniMessage().serialize(signLineComponent);

                    guiButtons.add(new ClickEventButton(finalI, InventoryUtils.createItem(Material.PAPER, 1, Translate.miniMessage("<dark_green>Edit line <yellow>" + (finalI + 1))), (guiClickEvent -> {
                        Component component = Component.empty()
                                .append(Translate.miniMessage("<red><bold>[CLICK ME TO GET THE CURRENT TEXT!]"))
                                .clickEvent(ClickEvent.suggestCommand(currentLineFormatted));
                        player.sendMessage(component);

                        chatResponseManager.create(player, (player1, s) -> {
                            // Update with new text.
                            signSide.line(finalI, isRegular ? LegacyComponentSerializer.legacyAmpersand().deserialize(s) : MiniMessage.miniMessage().deserialize(s));
                            sign.update();
                            player1.sendMessage(Translate.miniMessage("<green>Line " + (finalI + 1) + " has been updated."));

                            // Way to revert changes
                            player1.sendMessage(Translate.miniMessage("<yellow>Click me to revert change").clickEvent(chatClickCallbackManager.create(player, (player2 -> {
                                signSide.line(finalI, signLineComponent);
                                sign.update();
                                player2.sendMessage(Translate.miniMessage("<green>Line " + (finalI + 1) + " has been reverted."));
                            }))));
                        });
                        player.closeInventory();
                    })));
                }

                List<Component> options = Arrays.asList(Translate.miniMessage("<red>off"), Translate.miniMessage("<green>on"));
                guiButtons.add(new LoreShifterButton(8, InventoryUtils.createItem(Material.GLOW_INK_SAC, 1, Translate.miniMessage("<dark_green><bold>Set Glow").decoration(TextDecoration.ITALIC, false)), options, false, (guiClickEvent, number) -> {
                    if (number == 0) {
                        signSide.setGlowingText(false);
                        sign.update();
                        player.sendMessage(Translate.miniMessage("<green>Glowing text has been turned off."));
                    } else {
                        signSide.setGlowingText(true);
                        sign.update();
                        player.sendMessage(Translate.miniMessage("<green>Glowing text has been turned on."));
                    }
                }));

                this.update(guiButtons, Component.text("Click line to edit!"), 9);
            }

            @Override
            public void onClose(InventoryCloseEvent inventoryCloseEvent) {

            }
        };

        guiWindow.open(player);
    }
}
