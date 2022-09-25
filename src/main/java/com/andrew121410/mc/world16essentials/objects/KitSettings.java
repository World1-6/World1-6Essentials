package com.andrew121410.mc.world16essentials.objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class KitSettings implements ConfigurationSerializable {
    private String cooldown;
    private String permission;

    public KitSettings(String cooldown, String permission) {
        this.cooldown = cooldown;
        this.permission = permission;
    }

    public String getCooldown() {
        return cooldown;
    }

    public void setCooldown(String cooldown) {
        this.cooldown = cooldown;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("cooldown", this.cooldown);
        map.put("permission", this.permission);
        return map;
    }

    public static KitSettings deserialize(Map<String, Object> map) {
        return new KitSettings((String) map.get("cooldown"), (String) map.get("permission"));
    }
}