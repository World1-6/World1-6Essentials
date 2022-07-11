package com.andrew121410.mc.world16essentials.utils;

import com.Zrips.CMI.CMI;
import com.andrew121410.mc.world16essentials.World16Essentials;
import com.earth2me.essentials.Essentials;
import org.bukkit.plugin.Plugin;

public class OtherPlugins {

    private final World16Essentials plugin;

    // Soft Depend
    private Essentials essentials;
    private CMI cmi;

    public OtherPlugins(World16Essentials plugin) {
        this.plugin = plugin;

        // Soft Depend
        setupEssentials();
        setupCMI();
    }

    private void setupEssentials() {
        Plugin plugin = this.plugin.getServer().getPluginManager().getPlugin("Essentials");

        if (plugin == null) {
            this.essentials = null;
            return;
        }

        if (plugin instanceof Essentials) {
            this.essentials = (Essentials) plugin;
        }
    }

    private void setupCMI() {
        Plugin plugin = this.plugin.getServer().getPluginManager().getPlugin("CMI");

        if (plugin == null) {
            this.cmi = null;
            return;
        }

        if (plugin instanceof CMI) {
            this.cmi = (CMI) plugin;
        }
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public CMI getCmi() {
        return cmi;
    }
}
