package com.andrew121410.mc.world16utils.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static Block getBlockPlayerIsLookingAt(Player player) {
        return player.getTargetBlock(null, 5);
    }

    public static Block getBlockPlayerIsLookingAt(Player player, int range) {
        return player.getTargetBlock(null, range);
    }
}
