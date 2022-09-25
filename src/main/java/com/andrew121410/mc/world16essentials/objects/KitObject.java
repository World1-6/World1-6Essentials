package com.andrew121410.mc.world16essentials.objects;

import java.util.UUID;

public class KitObject {

    private final String kitName;
    private final UUID whoCreatedUUID;
    private final String timeCreated;
    private String regularInventoryBase64;
    private String armorContentBase64;

    public KitObject(String kitName, UUID whoCreatedUUID, String timeCreated, String regularInventoryBase64, String armorContentBase64) {
        this.kitName = kitName;
        this.whoCreatedUUID = whoCreatedUUID;
        this.timeCreated = timeCreated;
        this.regularInventoryBase64 = regularInventoryBase64;
        this.armorContentBase64 = armorContentBase64;
    }

    public KitObject(String kitName, UUID whoCreatedUUID, String timeCreated, String[] data) {
        this(kitName, whoCreatedUUID, timeCreated, data[0], data[1]);
    }

    public String getKitName() {
        return kitName;
    }

    public UUID getWhoCreatedUUID() {
        return whoCreatedUUID;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getRegularInventoryBase64() {
        return regularInventoryBase64;
    }

    public String getArmorContentBase64() {
        return armorContentBase64;
    }

    public void setRegularInventoryBase64(String regularInventoryBase64) {
        this.regularInventoryBase64 = regularInventoryBase64;
    }

    public void setArmorContentBase64(String armorContentBase64) {
        this.armorContentBase64 = armorContentBase64;
    }
}
