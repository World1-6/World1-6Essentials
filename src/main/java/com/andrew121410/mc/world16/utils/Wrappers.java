package com.andrew121410.mc.world16.utils;

import com.andrew121410.mc.world16.Main;
import com.andrew121410.mc.world16utils.blocks.BlockUtils;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_12_R1;
import com.andrew121410.mc.world16utils.blocks.BlockUtils_V1_16_R1;
import com.andrew121410.mc.world16utils.blocks.sign.SignUtils;
import com.andrew121410.mc.world16utils.blocks.sign.SignUtils_V1_12_R1;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils_V1_12_R1;
import com.andrew121410.mc.world16utils.enchantment.EnchantmentUtils_V1_16_R1;
import com.andrew121410.mc.world16utils.player.SmoothTeleport;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_12_R1;
import com.andrew121410.mc.world16utils.player.SmoothTeleport_V1_16_R1;

public class Wrappers {

    private Main plugin;

    public BlockUtils blockUtils;
    private SignUtils signUtils;
    private SmoothTeleport smoothTeleport;
    private EnchantmentUtils enchantmentUtils;

    public Wrappers(Main plugin) {
        switch (plugin.getApi().getServerVersion()) {
            case "1.16":
                this.blockUtils = new BlockUtils_V1_16_R1();
                this.signUtils = new SignUtils_V1_12_R1(plugin);
                this.smoothTeleport = new SmoothTeleport_V1_16_R1();
                this.enchantmentUtils = new EnchantmentUtils_V1_16_R1();
            case "1.12":
                this.blockUtils = new BlockUtils_V1_12_R1();
                this.signUtils = new SignUtils_V1_12_R1(plugin);
                this.smoothTeleport = new SmoothTeleport_V1_12_R1();
                this.enchantmentUtils = new EnchantmentUtils_V1_12_R1();
        }
    }

    public BlockUtils getBlockUtils() {
        return blockUtils;
    }

    public SignUtils getSignUtils() {
        return signUtils;
    }

    public SmoothTeleport getSmoothTeleport() {
        return smoothTeleport;
    }

    public EnchantmentUtils getEnchantmentUtils() {
        return enchantmentUtils;
    }
}
