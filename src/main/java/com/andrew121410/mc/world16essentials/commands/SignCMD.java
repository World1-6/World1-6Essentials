package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.blocks.UniversalBlockUtils;
import com.andrew121410.mc.world16utils.chat.ChatResponseManager;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.gui.GUIWindow;
import com.andrew121410.mc.world16utils.gui.buttons.AbstractGUIButton;
import com.andrew121410.mc.world16utils.gui.buttons.defaults.ClickEventButton;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import com.andrew121410.mc.world16utils.utils.InventoryUtils;
import com.andrew121410.mc.world16utils.utils.xutils.XMaterial;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SignCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public SignCMD(World16Essentials plugin) {
        this.plugin = plugin;

        this.api = this.plugin.getApi();
        this.plugin.getCommand("sign").setExecutor(this);
        this.plugin.getCommand("sign").setTabCompleter((commandSender, command, s, args) -> {
            if (args.length == 1) return Arrays.asList("give", "edit", "edita");
            if (args.length == 2 && args[0].equalsIgnoreCase("edita")) return Arrays.asList("@regular", "@minimessage");
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
            ItemStack item1 = new ItemStack(Objects.requireNonNull(XMaterial.OAK_SIGN.parseMaterial()), 1);
            item1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            player.getInventory().addItem(item1);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("edit")) {
            Block block = PlayerUtils.getBlockPlayerIsLookingAt(player);
            BlockState state = block.getState();

            if (!(state instanceof Sign sign)) {
                player.sendMessage(Translate.chat("&4This isn't a sign."));
                return true;
            }

            UniversalBlockUtils.editSign(player, sign);
            return true;
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("edita")) {
            Block block = PlayerUtils.getBlockPlayerIsLookingAt(player);
            BlockState state = block.getState();

            if (!(state instanceof Sign sign)) {
                player.sendMessage(Translate.chat("&4This isn't a sign."));
                return true;
            }

            if (args.length == 2 && args[1].equalsIgnoreCase("@minimessage")) {
                editGUI(player, sign, false);
            } else {
                editGUI(player, sign, true);
            }

        } else {
            player.sendMessage(Translate.chat("&cUsage: /sign <give|edit|edita>"));
        }
        return true;
    }

    private void editGUI(Player player, Sign sign, boolean isRegular) {
        GUIWindow guiWindow = new GUIWindow() {
            @Override
            public void onCreate(Player player) {
                List<AbstractGUIButton> guiButtons = new ArrayList<>();

                for (int i = 0; i < sign.lines().size(); i++) {
                    int finalI = i;
                    Component signLineComponent = sign.line(finalI);
                    String currentLineFormatted = isRegular ? LegacyComponentSerializer.legacyAmpersand().serialize(signLineComponent) : MiniMessage.miniMessage().serialize(signLineComponent);

                    guiButtons.add(new ClickEventButton(finalI, InventoryUtils.createItem(Material.PAPER, 1, "Edit line " + (finalI + 1)), (guiClickEvent -> {
                        Component component = Component.empty()
                                .append(Translate.miniMessage("<red><bold>[CLICK ME TO GET THE CURRENT TEXT!]"))
                                .clickEvent(ClickEvent.suggestCommand(currentLineFormatted));
                        player.sendMessage(component);

                        ChatResponseManager chatResponseManager = plugin.getOtherPlugins().getWorld16Utils().getChatResponseManager();
                        chatResponseManager.create(player, null, null, (player1, s) -> {
                            sign.line(finalI, isRegular ? LegacyComponentSerializer.legacyAmpersand().deserialize(s) : MiniMessage.miniMessage().deserialize(s));
                            sign.update();
                            player1.sendMessage(Translate.miniMessage("<green>Line " + (finalI + 1) + " has been updated."));
                        });
                    })));
                }

                this.update(guiButtons, "Click line to edit!", 9);
            }

            @Override
            public void onClose(InventoryCloseEvent inventoryCloseEvent) {

            }
        };

        guiWindow.open(player);
    }
}
