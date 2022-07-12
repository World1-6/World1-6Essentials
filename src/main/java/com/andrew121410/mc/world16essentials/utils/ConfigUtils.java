package com.andrew121410.mc.world16essentials.utils;

import com.andrew121410.mc.world16essentials.World16Essentials;

public class ConfigUtils {

    private final World16Essentials plugin;

    private boolean signTranslateColors;
    private boolean preventCropsTrampling;
    private int spawnMobCap;

    public ConfigUtils(World16Essentials plugin) {
        this.plugin = plugin;

        this.signTranslateColors = this.plugin.getConfig().getBoolean("signTranslateColors");
        this.preventCropsTrampling = this.plugin.getConfig().getBoolean("preventCropsTrampling");
        this.spawnMobCap = this.plugin.getConfig().getInt("spawnMobCap");
    }

    public boolean isSignTranslateColors() {
        return signTranslateColors;
    }

    public void setSignTranslateColors(boolean signTranslateColors) {
        this.signTranslateColors = signTranslateColors;
        this.plugin.getConfig().set("signTranslateColors", signTranslateColors);
        this.plugin.saveConfig();
    }

    public boolean isPreventCropsTrampling() {
        return preventCropsTrampling;
    }

    public void setPreventCropsTrampling(boolean preventCropsTrampling) {
        this.preventCropsTrampling = preventCropsTrampling;
        this.plugin.getConfig().set("preventCropsTrampling", preventCropsTrampling);
        this.plugin.saveConfig();
    }

    public int getSpawnMobCap() {
        return spawnMobCap;
    }

    public void setSpawnMobCap(int spawnMobCap) {
        this.spawnMobCap = spawnMobCap;
        this.plugin.getConfig().set("spawnMobCap", spawnMobCap);
        this.plugin.saveConfig();
    }
}
