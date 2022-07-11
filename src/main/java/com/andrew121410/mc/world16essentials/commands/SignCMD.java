package com.andrew121410.mc.world16essentials.commands;

import com.andrew121410.mc.world16essentials.World16Essentials;
import com.andrew121410.mc.world16essentials.utils.API;
import com.andrew121410.mc.world16utils.chat.Translate;
import com.andrew121410.mc.world16utils.player.PlayerUtils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class SignCMD implements CommandExecutor {

    private final World16Essentials plugin;
    private final API api;

    public SignCMD(World16Essentials plugin) {
        this.plugin = plugin;

        this.api = this.plugin.getApi();
        this.plugin.getCommand("sign").setExecutor(this);
        this.plugin.getCommand("sign").setTabCompleter((commandSender, command, s, args) -> {
            if (args.length == 1) return Arrays.asList("give", "edit");
            return null;
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players Can Use This Command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("world16.sign")) {
            api.sendPermissionErrorMessage(player);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Translate.chat("&e[Sign]&6 Sign help"));
            player.sendMessage(Translate.chat("&6/sign give &9<Gives you a sign>"));
            player.sendMessage(Translate.chat("&6/sign edit &9<Edits sign>"));
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("give")) {
            ItemStack item1 = new ItemStack(Objects.requireNonNull(Material.SIGN), 1);
            item1.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
            player.getInventory().addItem(item1);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("edit")) {
            Block block = PlayerUtils.getBlockPlayerIsLookingAt(player);

            BlockState blockState = block.getState();
            if (!(blockState instanceof Sign)) {
                player.sendMessage(Translate.chat("&e[Sign]&6 You must be looking at a sign."));
                return true;
            }

            Sign sign = (Sign) blockState;
            edit(player, sign);
        }
        return true;
    }

    public void edit(Player player, Sign sign) {
        Location loc = sign.getLocation();
        BlockPosition pos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        TileEntitySign tileEntitySign = (TileEntitySign) nmsPlayer.world.getTileEntity(pos);
        PlayerConnection conn = nmsPlayer.playerConnection;

        tileEntitySign.isEditable = true;
        tileEntitySign.a(nmsPlayer);
        conn.sendPacket(new PacketPlayOutOpenSignEditor(pos));
    }
}
